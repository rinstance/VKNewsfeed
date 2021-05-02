package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.domain.MainInteractor
import com.example.domain.PostInteractor
import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.interfaces.ApiRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun providePostInteractor(
        apiRepository: ApiRepository,
        databaseRepository: DatabaseRepository,
        preference: SharedPreferences
    ): PostInteractor =
        PostInteractor(apiRepository, databaseRepository, preference)

    @Provides
    @Singleton
    fun provideMainInteractor(preference: SharedPreferences): MainInteractor = MainInteractor(preference)

    @Provides
    @Singleton
    fun providePreferenceManager(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}