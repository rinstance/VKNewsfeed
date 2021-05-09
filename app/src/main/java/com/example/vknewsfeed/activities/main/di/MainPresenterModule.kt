package com.example.vknewsfeed.activities.main.di

import com.example.domain.MainInteractor
import com.example.domain.PostInteractor
import com.example.vknewsfeed.activities.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainPresenterModule {
    @Provides
    fun provideMainPresenter(
        mainInteractor: MainInteractor,
        postInteractor: PostInteractor
    ): MainPresenter =
        MainPresenter(mainInteractor, postInteractor)
}