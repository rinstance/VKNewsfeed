package com.example.vknewsfeed.favorites.di

import com.example.vknewsfeed.di.MainScope
import com.example.vknewsfeed.favorites.FavouritesFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [FavouritesViewModelModule::class]
)
@MainScope
interface FavouritesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: FavouritesFragment): FavouritesComponent
    }

    fun inject(fragment: FavouritesFragment)
}