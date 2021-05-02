package com.example.vknewsfeed.favorites.di

import androidx.lifecycle.ViewModelProvider
import com.example.domain.PostInteractor
import com.example.vknewsfeed.NewsMainViewModel
import com.example.vknewsfeed.di.ViewModelKey
import com.example.vknewsfeed.di.ViewModelModule
import com.example.vknewsfeed.favorites.FavouritesFragment
import com.example.vknewsfeed.favorites.FavouritesViewModel
import com.example.vknewsfeed.routers.AppRouter
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class])
class FavouritesViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    fun provideFavouritesViewModel(postInteractor: PostInteractor, router: AppRouter): NewsMainViewModel =
        FavouritesViewModel(postInteractor, router)

    @Provides
    fun provideViewModelCreator(
        fragment: FavouritesFragment,
        viewModelFactory: ViewModelProvider.Factory
    ): FavouritesViewModel {
        return ViewModelProvider(
            fragment.requireActivity(),
            viewModelFactory
        ).get(FavouritesViewModel::class.java)
    }
}
