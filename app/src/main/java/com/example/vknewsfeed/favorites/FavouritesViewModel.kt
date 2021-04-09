package com.example.vknewsfeed.favorites

import androidx.lifecycle.viewModelScope
import com.example.domain.PostInteractor
import com.example.domain.models.db.PostLocal
import com.example.vknewsfeed.MainViewModel
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(
    private val postInteractor: PostInteractor,
    private val router: AppRouter
) : MainViewModel(postInteractor) {

    suspend fun getSavedPosts(): Flow<List<PostLocal>> =
        withContext(coroutineContext) { postInteractor.getAllSavedPosts()}

    fun deleteAllPosts() {
        viewModelScope.launch(coroutineContext) {
            postInteractor.deleteAllSavedPosts()
        }
    }

    fun openDetailFragment(postId: Int, sourceId: Int) {
        router.openDetailFragment(postId, sourceId)
    }

    fun deletePost(id: Int) {
        viewModelScope.launch(coroutineContext) { postInteractor.deleteSavedPost(id) }
    }
}