package com.example.vknewsfeed.newsfeed.di

import com.example.domain.models.api.Post
import com.example.vknewsfeed.di.MainScope
import com.example.vknewsfeed.newsfeed.NewsfeedFragment
import com.example.vknewsfeed.routers.AppRouter
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(
    modules = [
        NewsfeedViewModelModule::class,
        NewsfeedAdapterModule::class
    ]
)
@MainScope
interface NewsfeedComponent {

    @Subcomponent.Builder
    interface Factory {
        fun itemClick(
            @BindsInstance
            @Named("itemClick")
            itemClick: (Post) -> Unit
        ): Factory

        fun longClick(
            @BindsInstance
            @Named("longClick")
            likeAction: (Post) -> Unit
        ): Factory

        fun likeAction(
            @BindsInstance
            @Named("likeAction")
            likeAction: (Post) -> Unit
        ): Factory

        fun router(@BindsInstance router: AppRouter): Factory

        fun create(@BindsInstance fragment: NewsfeedFragment): Factory

        fun build(): NewsfeedComponent
    }

    fun inject(fragment: NewsfeedFragment)
}