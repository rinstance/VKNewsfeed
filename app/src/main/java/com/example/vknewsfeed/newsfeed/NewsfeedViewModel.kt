package com.example.vknewsfeed.newsfeed

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.domain.PostInteractor
import com.example.domain.helpers.Constants
import com.example.domain.models.api.Post
import com.example.domain.models.api.SavedPost
import com.example.vknewsfeed.MainViewModel
import com.example.vknewsfeed.R
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import okhttp3.MultipartBody
import javax.inject.Provider

class NewsfeedViewModel(
    private val postInteractor: PostInteractor,
    private val router: AppRouter,
    private val dataSource: NewsfeedPageKeyedDataSource,
    private val provider: Provider<PagedList<Post>>
) : MainViewModel(postInteractor) {
    val mutableLoading = MutableLiveData<Boolean>()
    val mutablePosts = MutableLiveData<PagedList<Post>>()
    var posts: PagedList<Post> = provider.get()

    init {
        subscribeOnLoading()
    }

    private suspend fun createPost(photo: MultipartBody.Part?, message: String): SavedPost {
        return withContext(coroutineContext) { postInteractor.createPost(photo, message) }
    }

    private fun subscribeOnLoading() {
        viewModelScope.launch {
            dataSource.loadingProgress.consumeEach { isLoading ->
                mutableLoading.postValue(isLoading)
                mutablePosts.postValue(posts)
            }
        }
    }

    fun savePost(post: Post) {
        viewModelScope.launch(coroutineContext) {
            postInteractor.savePost(post)
            withContext(Dispatchers.Main) { router.showToast(R.string.successful) }
        }
    }

    fun openDetailFragment(postId: Int, sourceId: Int) {
        router.openDetailFragment(postId, sourceId)
    }

    fun createPostAndShow(attachPhoto: MultipartBody.Part?, message: String) {
        viewModelScope.launch(coroutineContext) {
            router.showProgressBar()
            val postId = createPost(attachPhoto, message).response.postId
            router.hideProgressBar()
            openDetailFragment(Constants.USER_ID, postId)
        }
    }

    fun updateLikes(bundle: Bundle) {
        val postId = bundle.getInt(Constants.INTENT_POST_ID, 0)
        val likeCount = bundle.getInt(Constants.INTENT_LIKE_COUNT, 0)
        val userLike = bundle.getInt(Constants.INTENT_USER_LIKE, 0)
        posts.forEach { item ->
            if (item.postId == postId) {
                item.likes.count = likeCount
                item.likes.userLikes = userLike
                mutableItemAfterLike.postValue(item)
                return
            }
        }
    }

    fun logout() {
        router.openAuthActivity()
    }

    fun setFilterType(type: String) {
        postInteractor.setFilterType(type)
        updateList()
    }

    private fun updateList() {
        posts = provider.get()
        mutablePosts.postValue(posts)
    }
}