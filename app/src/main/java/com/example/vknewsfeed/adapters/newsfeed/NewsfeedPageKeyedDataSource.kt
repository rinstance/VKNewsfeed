package com.example.vknewsfeed.adapters.newsfeed

import androidx.paging.PageKeyedDataSource
import com.example.data.helpers.Constants
import com.example.domain.PostInteractor
import com.example.domain.models.api.Newsfeed
import com.example.domain.models.api.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewsfeedPageKeyedDataSource(
        private val postInteractor: PostInteractor
) : PageKeyedDataSource<String, Post>(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
    val loadingProgress = Channel<Boolean>()
    private var isInitialLoading = true
    private lateinit var initialCallback: LoadInitialCallback<String, Post>
    private lateinit var nextCallback: LoadCallback<String, Post>

    init {
//        EspressoIdlingResource.increment() //for test
    }

    private suspend fun showLoadingProgressBar() = loadingProgress.send(true)

    private suspend fun hideLoadingProgressBar() = loadingProgress.send(false)

    private fun showNews(newsfeeed: Newsfeed, items: List<Post>) {
        if (isInitialLoading) {
            initialCallback.onResult(items, null, newsfeeed.nextFrom)
            isInitialLoading = false
//            EspressoIdlingResource.decrement() //for test
        } else {
            nextCallback.onResult(items, newsfeeed.nextFrom)
        }
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Post>) {
        initialCallback = callback
        launch(coroutineContext) {
            showLoadingProgressBar()
            val responseNewsfeed = postInteractor.getNewsfeed(params.requestedLoadSize)
            val posts = responseNewsfeed.items
            postInteractor.getAuthors(posts)
            getVideos(posts)
            showNews(responseNewsfeed, posts)
            hideLoadingProgressBar()
        }
    }

    private suspend fun getVideos(posts: List<Post>) {
        posts.forEach { post ->
            post.attachments?.forEach {
                if (it.type == Constants.ATTACHMENTS_VIDEO_TYPE)
                    it.video = postInteractor.getVideo("${it.video.ownerId}_${it.video.id}")
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Post>) {
        nextCallback = callback
        launch(coroutineContext) {
            showLoadingProgressBar()
            val responseNewsfeed =
                postInteractor.getNextNewsfeed(params.key, params.requestedLoadSize * 3)
            val posts = responseNewsfeed.items
            postInteractor.getAuthors(posts)
            getVideos(posts)
            showNews(responseNewsfeed, posts)
            hideLoadingProgressBar()
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Post>) {

    }
}
