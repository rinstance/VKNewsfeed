package com.example.vknewsfeed.detail

import com.example.domain.PostInteractor
import com.example.domain.models.api.Post
import com.example.vknewsfeed.NewsMainViewModel
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val postInteractor: PostInteractor
) : NewsMainViewModel(postInteractor) {

    suspend fun getPostById(sourceId: Int, postId: Int) =
        withContext(coroutineContext) { postInteractor.getPostById("${sourceId}_${postId}") }

    suspend fun getAuthors(posts: List<Post>) {
        withContext(coroutineContext) { postInteractor.getAuthors(posts) }
    }
}
