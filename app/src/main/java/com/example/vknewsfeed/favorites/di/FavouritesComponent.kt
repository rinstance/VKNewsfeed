package com.example.vknewsfeed.favorites.di

import com.example.vknewsfeed.di.MainScope
import com.example.vknewsfeed.favorites.FavouritesFragment
import com.example.vknewsfeed.routers.AppRouter
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [FavouritesViewModelModule::class]
)
@MainScope
interface FavouritesComponent {

    @Subcomponent.Builder
    interface Factory {
        fun router(@BindsInstance router: AppRouter): Factory
        fun create(@BindsInstance fragment: FavouritesFragment): Factory
        fun build(): FavouritesComponent
    }

    fun inject(fragment: FavouritesFragment)
}