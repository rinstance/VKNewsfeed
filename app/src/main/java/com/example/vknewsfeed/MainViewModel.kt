package com.example.vknewsfeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.PostInteractor
import com.example.domain.models.api.Post
import com.example.domain.models.api.Video
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class MainViewModel(
    private val postInteractor: PostInteractor
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
    val mutableItemAfterLike = MutableLiveData<Post>()

    fun setLike(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = postInteractor.setLike(post)
            post.likes.apply {
                count = likes
                userLikes = 1
            }
            mutableItemAfterLike.postValue(post)
        }
    }

    fun deleteLike(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = postInteractor.deleteLike(post)
            post.likes.apply {
                count = likes
                userLikes = 0
            }
            mutableItemAfterLike.postValue(post)
        }
    }

    suspend fun getVideo(video: Video): Video =
        withContext(coroutineContext) { postInteractor.getVideo("${video.ownerId}_${video.id}") }
}