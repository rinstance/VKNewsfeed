package com.example.vknewsfeed.newsfeed.di

import androidx.paging.PagedList
import com.example.domain.helpers.Constants
import com.example.domain.PostInteractor
import com.example.domain.models.api.Post
import com.example.vknewsfeed.di.MainScope
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.PostDiffUtilCallback
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedAdapter
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
import com.example.vknewsfeed.helpers.MainThreadExecutor
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
class NewsfeedAdapterModule {

    @Provides
    fun getDiffUtil(): PostDiffUtilCallback = PostDiffUtilCallback()

    @Provides
    fun getAdapter(
        @Named("itemClick") itemClick: (Post) -> Unit,
        @Named("likeAction") likeAction: (Post) -> Unit,
        @Named("longClick") longClick: (Post) -> Unit,
        callback: PostDiffUtilCallback
    ): NewsfeedAdapter = NewsfeedAdapter(callback, itemClick, likeAction, longClick)

    @Provides
    fun getConfig(): PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(Constants.COUNT_FOR_NEWS)
        .build()

    @Provides
    @MainScope
    fun getDataSource(postInteractor: PostInteractor): NewsfeedPageKeyedDataSource =
        NewsfeedPageKeyedDataSource(postInteractor)

    @Provides
    fun getItems(
        config: PagedList.Config,
        dataSource: NewsfeedPageKeyedDataSource
    ): PagedList<Post> =
        PagedList.Builder(dataSource, config)
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
}
