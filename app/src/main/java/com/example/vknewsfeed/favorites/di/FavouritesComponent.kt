package com.example.vknewsfeed.favorites.di

import com.example.vknewsfeed.di.MainScope
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [FavouritesViewModelModule::class]
)
@MainScope
interface FavouritesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: FavouritesComponent): FavouritesComponent
    }

    fun inject(fragment: FavouritesComponent)
}