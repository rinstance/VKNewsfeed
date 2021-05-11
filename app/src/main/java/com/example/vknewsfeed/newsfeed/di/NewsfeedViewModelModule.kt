package com.example.vknewsfeed.newsfeed.di

import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.example.domain.PostInteractor
import com.example.domain.models.api.Post
import com.example.vknewsfeed.MainViewModel
import com.example.vknewsfeed.di.ViewModelKey
import com.example.vknewsfeed.di.ViewModelModule
import com.example.vknewsfeed.newsfeed.NewsfeedFragment
import com.example.vknewsfeed.newsfeed.NewsfeedViewModel
import com.example.vknewsfeed.newsfeed.adapters.newsfeed.NewsfeedPageKeyedDataSource
import com.example.vknewsfeed.routers.AppRouter
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module(includes = [ViewModelModule::class])
class NewsfeedViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(NewsfeedViewModel::class)
    fun provideNewsViewModel(
        postInteractor: PostInteractor,
        router: AppRouter,
        dataSource: NewsfeedPageKeyedDataSource,
        provider: Provider<PagedList<Post>>
    ): MainViewModel {
        return NewsfeedViewModel(postInteractor, router, dataSource, provider)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: NewsfeedFragment,
        viewModelFactory: ViewModelProvider.Factory
    ): NewsfeedViewModel =
        ViewModelProvider(
            fragment.requireActivity(),
            viewModelFactory
        ).get(NewsfeedViewModel::class.java)
}