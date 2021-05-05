package com.example.vknewsfeed.newsfeed

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagedList
import com.example.domain.helpers.Constants
import com.example.domain.models.api.Post
import com.example.vknewsfeed.App
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.main.MainActivity
import com.example.vknewsfeed.fragments.InfoDialogFragment
import com.example.vknewsfeed.fragments.NewPostDialogFragment
import com.example.vknewsfeed.fragments.ProgressDialogFragment
import com.example.vknewsfeed.helpers.DialogConstants
import com.example.vknewsfeed.helpers.getNavigationResult
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedAdapter
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
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
    private lateinit var loadingProgressChannel: Channel<Boolean>
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
        App.appComponent.newsComponentBuilder()
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
        dialog.show(childFragmentManager, DialogConstants.SAVE_DIALOG)
        dialog.setMessage(resources.getString(R.string.save_post_message))
        dialog.setOKListener(object : InfoDialogFragment.Listener {
            override fun ok() = savePost(post)
        })
    }

    private fun savePost(post: Post) {
        model.savePost(post)
    }

    private fun likeAction(post: Post) {
        model.doLike(post)
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
        icon_add_post?.setOnClickListener { createNewPostDialog() }
        icon_log_out?.setOnClickListener { showLogoutDialog() }
        icon_filters?.setOnClickListener { popupFilters(it) }
        action_back.visibility = View.GONE
        (toolbar_title as TextView).setText(R.string.newsfeed)
    }

    private fun popupFilters(view: View) {
        val popupMenu = PopupMenu(parentActivity, view)
        popupMenu.inflate(R.menu.menu_filters)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.onlyPhotos -> {
                    setContentFilter(Constants.ATTACHMENTS_PHOTO_TYPE)
                    true
                }
                R.id.onlyVideos -> {
                    setContentFilter(Constants.ATTACHMENTS_VIDEO_TYPE)
                    true
                }
                R.id.allFilters -> {
                    setContentFilter(Constants.ATTACHMENTS_ALL_TYPE)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun setContentFilter(type: String) {
        PreferenceManager.getDefaultSharedPreferences(parentActivity)
            .edit()
            .putString(Constants.PREF_CONTENT_FILTER, type)
            .apply()
    }

    private fun showLogoutDialog() {
        val dialog = InfoDialogFragment()
        dialog.show(childFragmentManager, DialogConstants.LOGOUT)
        dialog.setMessage(resources.getString(R.string.are_you_sure))
        dialog.setOkText(resources.getString(R.string.log_out))
        dialog.setOKListener(object : InfoDialogFragment.Listener {
            override fun ok() = logout()
        })
    }

    private fun createNewPostDialog() {
        attachPhoto = null
        newPostDialog = NewPostDialogFragment()
        newPostDialog.show(childFragmentManager, DialogConstants.CREATE_POST)
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
        model.createPostAndShow(attachPhoto, message)
    }

    private fun chooseImage() {
        if (requestStoragePermission()) {
            Intent().apply {
                type = Constants.IMAGE_TYPE_INTENT
                action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(this, resources.getString(R.string.select_image)),
                    REQUEST_CHOOSE_IMAGE
                )
            }
        }
    }

    private fun requestStoragePermission(): Boolean {
        val context = context ?: return false
        val activity = activity ?: return false
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_CONTACTS
            )
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
            RequestBody.create(MediaType.parse(imageUri?.let {
                parentActivity.contentResolver?.getType(
                    it
                )
            }), file)
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
        model.logout();
    }
}
