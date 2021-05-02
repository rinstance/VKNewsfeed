
package com.example.vknewsfeed.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.domain.models.db.PostLocal
import com.example.vknewsfeed.App
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.main.MainActivity
import com.example.vknewsfeed.favorites.adapters.FavouritePostsAdapter
import com.example.vknewsfeed.fragments.InfoDialogFragment
import com.example.vknewsfeed.fragments.ProgressDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FavouritesFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main
    private lateinit var adapter: FavouritePostsAdapter
    private lateinit var progressDialog: ProgressDialogFragment
    @Inject lateinit var model: FavouritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_favourites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDI()
        setView()
        launch(coroutineContext) { setPosts() }
    }

    private fun setDI() {
        App.appComponent.favouritesComponentBuilder()
            .create(this)
            .itemClick { startDetailFragment(it) }
            .longClick { showDeletePostDialog(it) }
            .router(activity as MainActivity)
            .build()
            .inject(this)
    }

    private suspend fun setPosts() {
        adapter = FavouritePostsAdapter(
            itemClick = { startDetailFragment(it) },
            longClick = { showDeletePostDialog(it) }
        )
        recycler_favourites?.adapter = this.adapter
        model.getSavedPosts().collect {
            if (this@FavouritesFragment.isVisible) {
                showProgressDialog()
                if (it.isEmpty())
                    text_no_posts?.visibility = View.VISIBLE
                adapter.submitList(it)
                hideProgressDialog()
            }
        }
    }

    private fun showDeletePostDialog(post: PostLocal) {
        val dialog = InfoDialogFragment()
        dialog.show(childFragmentManager, "deletePost")
        dialog.setMessage(resources.getString(R.string.DELETE_POST_MESSAGE))
        dialog.setOkText(resources.getString(R.string.DELETE))
        dialog.setOKListener(object : InfoDialogFragment.Listener {
            override fun ok() = model.deletePost(post.id)
        })
    }

    private fun startDetailFragment(post: PostLocal) {
        model.openDetailFragment(post.id, post.sourceId)
    }

    private fun setView() {
        activity?.bottom_nav_view?.visibility = View.VISIBLE
        action_back?.visibility = View.GONE
        toolbar_title?.text = resources.getString(R.string.FAVOURITES)
        button_delete_all?.setOnClickListener { deleteAllPosts() }
    }

    private fun deleteAllPosts() {
        model.deleteAllPosts()
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialogFragment()
        progressDialog.show(childFragmentManager, "loading")
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}
