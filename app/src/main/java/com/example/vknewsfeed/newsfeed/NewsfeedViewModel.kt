package com.example.vknewsfeed.newsfeed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.PostInteractor
import com.example.domain.helpers.Constants
import com.example.domain.models.api.Post
import com.example.domain.models.api.SavedPost
import com.example.vknewsfeed.NewsMainViewModel
import com.example.vknewsfeed.R
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import okhttp3.MultipartBody

class NewsfeedViewModel(
    private val postInteractor: PostInteractor,
    private val router: AppRouter,
    private val dataSource: NewsfeedPageKeyedDataSource
) : NewsMainViewModel(postInteractor) {
    var isLoadingPosts = true
    val mutableLoading = MutableLiveData<Boolean>()

    init {
//        subscribeOnLoading()
    }

    suspend fun createPost(photo: MultipartBody.Part?, message: String): SavedPost {
        return withContext(Dispatchers.IO) { postInteractor.createPost(photo, message) }
    }

    private fun subscribeOnLoading() {
        viewModelScope.launch {
            dataSource.loadingProgress.consumeEach { isLoading ->
                isLoadingPosts = isLoading
                mutableLoading.postValue(isLoading)
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

    fun logout() {
        router.openAuthActivity()
    }

    fun createPostAndShow(attachPhoto: MultipartBody.Part?, message: String) {
        viewModelScope.launch(coroutineContext) {
            router.showProgressBar()
            val postId = createPost(attachPhoto, message).response.postId
            router.hideProgressBar()
            openDetailFragment(Constants.USER_ID, postId)
        }
    }
}