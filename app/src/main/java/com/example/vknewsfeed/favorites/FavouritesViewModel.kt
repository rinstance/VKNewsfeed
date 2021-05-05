package com.example.vknewsfeed.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.PostInteractor
import com.example.domain.models.db.PostLocal
import com.example.vknewsfeed.NewsMainViewModel
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(
    private val postInteractor: PostInteractor,
    private val router: AppRouter
) : NewsMainViewModel(postInteractor) {
    val mutableSavedPosts = MutableLiveData<List<PostLocal>>()

    init {
        subscribeOnSavedPosts()
    }

    private fun subscribeOnSavedPosts() {
        viewModelScope.launch {
            postInteractor.getAllSavedPosts().collect { mutableSavedPosts.postValue(it) }
        }
    }

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