package com.example.vknewsfeed.detail.di

import androidx.lifecycle.ViewModelProvider
import com.example.domain.PostInteractor
import com.example.vknewsfeed.MainViewModel
import com.example.vknewsfeed.detail.DetailPostFragment
import com.example.vknewsfeed.detail.DetailViewModel
import com.example.vknewsfeed.di.ViewModelKey
import com.example.vknewsfeed.di.ViewModelModule
import com.example.vknewsfeed.newsfeed.NewsfeedFragment
import com.example.vknewsfeed.newsfeed.NewsfeedViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
class DetailViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun provideDetailViewModel(postInteractor: PostInteractor): MainViewModel {
        return DetailViewModel(postInteractor)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: DetailPostFragment,
        viewModelFactory: ViewModelProvider.Factory
    ): DetailViewModel {
        return ViewModelProvider(fragment.requireActivity(), viewModelFactory).get(DetailViewModel::class.java)
    }
}