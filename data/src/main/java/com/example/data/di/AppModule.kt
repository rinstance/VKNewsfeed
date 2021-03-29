package com.example.data.di

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
        databaseRepository: DatabaseRepository
    ): PostInteractor =
        PostInteractor(apiRepository, databaseRepository)
}