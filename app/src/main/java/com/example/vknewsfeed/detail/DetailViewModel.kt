package com.example.vknewsfeed.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.PostInteractor
import com.example.domain.helpers.Constants
import com.example.domain.models.api.Post
import com.example.vknewsfeed.NewsMainViewModel
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val postInteractor: PostInteractor,
    private val router: AppRouter
) : NewsMainViewModel(postInteractor) {
    val mutableDetailPost = MutableLiveData<Post?>()

    suspend fun getPostById(sourceId: Int, postId: Int) =
        withContext(coroutineContext) { postInteractor.getPostById("${sourceId}_${postId}") }

    suspend fun getAuthors(posts: List<Post>) {
        withContext(coroutineContext) { postInteractor.getAuthors(posts) }
    }

    fun setupPost(postId: Int?, sourceId: Int?) {
        if (postId != null && sourceId != null) {
            viewModelScope.launch(coroutineContext) {
                router.showProgressBar()
                val posts = getPostById(sourceId, postId)
                getAuthors(posts)
                val post = posts[0]
                post.postId = postId
                getVideoForPost(post)
                mutableDetailPost.postValue(post)
                router.hideProgressBar()
            }
        }
    }

    private suspend fun getVideoForPost(post: Post) {
        post.attachments?.forEach {
            if (it.type == Constants.ATTACHMENTS_VIDEO_TYPE) {
                getVideo(post, it)
            }
        }
    }

    fun setPostNull() {
        mutableDetailPost.postValue(null)
    }
}
