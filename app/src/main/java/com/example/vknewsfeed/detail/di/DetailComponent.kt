package com.example.vknewsfeed.detail.di

import com.example.vknewsfeed.detail.DetailPostFragment
import com.example.vknewsfeed.di.MainScope
import com.example.vknewsfeed.newsfeed.NewsfeedFragment
import com.example.vknewsfeed.newsfeed.di.NewsfeedAdapterModule
import com.example.vknewsfeed.newsfeed.di.NewsfeedComponent
import com.example.vknewsfeed.newsfeed.di.NewsfeedViewModelModule
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        DetailViewModelModule::class
    ]
)
@MainScope
interface DetailComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: DetailPostFragment): DetailComponent
    }

    fun inject(fragment: DetailPostFragment)
}