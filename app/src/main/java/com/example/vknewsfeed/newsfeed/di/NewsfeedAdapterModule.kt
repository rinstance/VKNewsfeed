package com.example.vknewsfeed.newsfeed.di

import androidx.paging.PagedList
import com.example.data.helpers.Constants
import com.example.domain.PostInteractor
import com.example.domain.models.api.Post
import com.example.vknewsfeed.adapters.newsfeed.ItemDiffUtilCallback
import com.example.vknewsfeed.adapters.newsfeed.NewsfeedAdapter
import com.example.vknewsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
import com.example.vknewsfeed.helpers.MainThreadExecutor
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Named

@Module
class NewsfeedAdapterModule {

    @Provides
    fun getDiffUtil(): ItemDiffUtilCallback = ItemDiffUtilCallback()

    @Provides
    fun getAdapter(
        @Named("itemClick") itemClick: (Post) -> Unit,
        @Named("likeAction") likeAction: (Post) -> Unit,
        callback: ItemDiffUtilCallback
    ): NewsfeedAdapter = NewsfeedAdapter(callback, itemClick, likeAction)

    @Provides
    fun getConfig(): PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(Constants.COUNT_FOR_NEWS)
        .build()

    @Provides
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
