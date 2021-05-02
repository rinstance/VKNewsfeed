package com.example.vknewsfeed.favorites.di

import com.example.domain.models.api.Post
import com.example.domain.models.db.PostLocal
import com.example.vknewsfeed.di.MainScope
import com.example.vknewsfeed.favorites.FavouritesFragment
import com.example.vknewsfeed.newsfeed.di.NewsfeedComponent
import com.example.vknewsfeed.routers.AppRouter
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(
    modules = [
        FavouritesViewModelModule::class
    ]
)
@MainScope
interface FavouritesComponent {

    @Subcomponent.Builder
    interface Factory {
        fun itemClick(
            @BindsInstance
            @Named("itemClick")
            itemClick: (PostLocal) -> Unit
        ): Factory

        fun longClick(
            @BindsInstance
            @Named("longClick")
            likeAction: (PostLocal) -> Unit
        ): Factory

        fun router(@BindsInstance router: AppRouter): Factory
        fun create(@BindsInstance fragment: FavouritesFragment): Factory
        fun build(): FavouritesComponent
    }

    fun inject(fragment: FavouritesFragment)
}