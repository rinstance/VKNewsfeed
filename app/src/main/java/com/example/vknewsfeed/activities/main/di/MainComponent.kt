package com.example.vknewsfeed.activities.main.di

import com.example.vknewsfeed.activities.main.MainActivity
import com.example.vknewsfeed.di.MainScope
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [MainPresenterModule::class]
)
@MainScope
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): MainComponent
    }

    fun inject(activity: MainActivity)
}