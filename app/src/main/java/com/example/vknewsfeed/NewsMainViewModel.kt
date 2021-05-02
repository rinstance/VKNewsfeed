package com.example.vknewsfeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.PostInteractor
import com.example.domain.models.api.Attachment
import com.example.domain.models.api.Post
import com.example.domain.models.api.Video
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class NewsMainViewModel(
    private val postInteractor: PostInteractor
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
    val mutableItemAfterLike = MutableLiveData<Post>()

    private fun setLike(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = postInteractor.setLike(post)
            post.likes.apply {
                count = likes
                userLikes = 1
            }
            mutableItemAfterLike.postValue(post)
        }
    }

    private fun deleteLike(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = postInteractor.deleteLike(post)
            post.likes.apply {
                count = likes
                userLikes = 0
            }
            mutableItemAfterLike.postValue(post)
        }
    }

    suspend fun getVideo(post: Post, attachment: Attachment) {
        postInteractor.getVideo(post, attachment)
    }

    fun doLike(post: Post) {
        if (post.likes.userLikes == 0)
            setLike(post)
        else
            deleteLike(post)
    }
}