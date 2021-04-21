package com.example.vknewsfeed.newsfeed

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagedList
import com.example.data.helpers.Constants
import com.example.domain.models.api.Post
import com.example.vknewsfeed.App
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.MainActivity
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedAdapter
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
import com.example.vknewsfeed.fragments.InfoDialogFragment
import com.example.vknewsfeed.fragments.NewPostDialogFragment
import com.example.vknewsfeed.fragments.ProgressDialogFragment
import com.example.vknewsfeed.helpers.getNavigationResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_newsfeed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val REQUEST_CHOOSE_IMAGE = 1
private const val REQUEST_CODE_PERMISSION_READ_CONTACTS = 2

class NewsfeedFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main
    private lateinit var parentActivity: FragmentActivity
    private lateinit var newPostDialog: NewPostDialogFragment
    private lateinit var progressDialog: ProgressDialogFragment
    private lateinit var loadingProgressChannel: Channel<Boolean>
    private lateinit var navController: NavController
    private var isLoadingPosts = false
    private var attachPhoto: MultipartBody.Part? = null
    @Inject lateinit var model: NewsfeedViewModel
    @Inject lateinit var items: PagedList<Post>
    @Inject lateinit var adapter: NewsfeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_newsfeed, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDI()
        launch(coroutineContext) { setLoadingProgressBar() }
        loadingProgressChannel = (items.dataSource as NewsfeedPageKeyedDataSource).loadingProgress
        navController = NavHostFragment.findNavController(this)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setIndicatorAfterRestored()
    }

    private fun setIndicatorAfterRestored() {
        if (isLoadingPosts) progress_indicator?.show()
        else progress_indicator?.hide()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initActivity()
        setupView()
        getNavigationResult(Constants.INTENT_DETAIL_DATA)?.observe(viewLifecycleOwner, Observer {
            updateLikes(it)
        })
    }

    private fun setupDI() {
        App.appComponent.newsComponentFactory()
            .create(this)
            .itemClick { startItemDetailFragment(it.sourceId, it.postId) }
            .likeAction { likeAction(it) }
            .longClick { showSavePostDialog(it) }
            .router(activity as MainActivity)
            .create(this)
            .build()
            .inject(this)
    }

    private fun showSavePostDialog(post: Post) {
        val dialog = InfoDialogFragment()
        dialog.setMessage(resources.getString(R.string.SAVE_POST_MESSAGE))
        dialog.setOKListener { savePost(post) }
        dialog.show(childFragmentManager, "saveDialog")
    }

    private fun savePost(post: Post) {
        model.savePost(post)
    }

    private fun likeAction(post: Post) {
        if (post.likes.userLikes == 0)
            model.setLike(post)
        else
            model.deleteLike(post)
    }

    private fun setupView() {
        parentActivity.bottom_nav_view?.visibility = View.VISIBLE
        setToolbar()
        setAdapter()
        updateAdapterAfterLike()
    }

    private suspend fun setLoadingProgressBar() {
        loadingProgressChannel.consumeEach { isLoading ->
            isLoadingPosts = isLoading
            if (isLoading)
                progress_indicator?.show()
            else
                progress_indicator?.hide()
        }
    }

    private fun initActivity() {
        val activity = activity ?: return
        this.parentActivity = activity
    }

    private fun setToolbar() {
        icon_add_post.setOnClickListener { createNewPostDialog() }
        icon_log_out.setOnClickListener { showLogoutDialog() }
        action_back.visibility = View.GONE
        (toolbar_title as TextView).setText(R.string.NEWSFEED)
    }

    private fun showLogoutDialog() {
        val dialog = InfoDialogFragment()
        dialog.setMessage(resources.getString(R.string.ARE_YOU_SURE))
        dialog.setOkText(resources.getString(R.string.LOG_OUT))
        dialog.setOKListener { logout() }
        dialog.show(childFragmentManager, "logoutDialog")
    }


    private fun createNewPostDialog() {
        attachPhoto = null
        newPostDialog = NewPostDialogFragment()
        newPostDialog.show(childFragmentManager, "createPost")
        newPostDialog.setClickListeners(object : NewPostDialogFragment.Listeners {

            override fun create(message: String) {
                newPostDialog.dismiss()
                createPostAndShow(message)
            }

            override fun attachImage(isSelected: Boolean) {
                if (!isSelected)
                    chooseImage()
                else {
                    newPostDialog.deleteImage()
                    attachPhoto = null
                }
            }
        })
    }

    private fun createPostAndShow(message: String) {
        launch(coroutineContext) {
            showProgressDialog()
            val postId = model.createPost(attachPhoto, message).response.postId
            hideProgressDialog()
            startItemDetailFragment(Constants.USER_ID, postId)
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialogFragment()
        progressDialog.show(childFragmentManager, "loading")
    }

    private fun hideProgressDialog() = progressDialog.dismiss()

    private fun chooseImage() {
        if (requestStoragePermission()) {
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(this, resources.getString(R.string.SELECT_IMAGE)), REQUEST_CHOOSE_IMAGE)
            }
        }
    }

    private fun requestStoragePermission(): Boolean {
        val context = context ?: return false
        val activity = activity ?: return false
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_CONTACTS)
            return false
        }
        return true
    }

    private fun startItemDetailFragment(sourceId: Int, postId: Int) {
        model.openDetailFragment(postId, sourceId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == AppCompatActivity.RESULT_OK)
                chooseImageResult(it)
        }
    }

    private fun updateLikes(bundle: Bundle) {
        val postId = bundle.getInt(Constants.INTENT_POST_ID, 0)
        val likeCount = bundle.getInt(Constants.INTENT_LIKE_COUNT, 0)
        val userLike = bundle.getInt(Constants.INTENT_USER_LIKE, 0)
        items.forEach { item ->
            if (item.postId == postId) {
                item.likes.count = likeCount
                item.likes.userLikes = userLike
                adapter.notifyItemChanged(items.indexOf(item))
                return
            }
        }
    }

    private fun chooseImageResult(data: Intent) {
        val imageUri = data.data
        val file = File(getPathFromURI(imageUri))
        val requestFile =
            RequestBody.create(MediaType.parse(imageUri?.let { parentActivity.contentResolver?.getType(it) }), file)
        attachPhoto = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        if (attachPhoto != null) newPostDialog.setImage(imageUri)
    }

    private fun getPathFromURI(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = parentActivity.managedQuery(uri, projection, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    private fun updateAdapterAfterLike() {
        model.mutableItemAfterLike.observe(viewLifecycleOwner, Observer { post: Post ->
            adapter.notifyItemChanged(items.indexOf(post))
        })
    }

    private fun setAdapter() {
        adapter.submitList(items)
        newsfeed_recyclerview.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_CONTACTS && permissions.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            chooseImage()
    }

    private fun logout() {
//        model.logout()
        PreferenceManager.getDefaultSharedPreferences(parentActivity)
            .edit().putBoolean(Constants.PREFERENCE_IS_AUTH, false).apply()
        val extras = ActivityNavigator.Extras.Builder()
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .build()
        navController.navigate(R.id.action_to_auth_activity)
//        startActivity(
//            Intent(this, AuthorizationActivity::class.java)
//            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}
