package com.example.vknewsfeed.activities.main.di

import android.content.SharedPreferences
import com.example.domain.MainInteractor
import com.example.vknewsfeed.activities.main.MainPresenter
import com.example.vknewsfeed.di.ViewModelModule
import dagger.Module
import dagger.Provides

@Module
class MainPresenterModule {
    @Provides
    fun provideMainPresenter(mainInteractor: MainInteractor): MainPresenter =
        MainPresenter(mainInteractor)
}