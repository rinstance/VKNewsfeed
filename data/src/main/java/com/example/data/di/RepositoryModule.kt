package com.example.data.di

import com.example.data.network.VKApi
import com.example.data.repository.api.ApiRepositoryImpl
import com.example.data.repository.db.DatabaseRepositoryImpl
import com.example.data.repository.db.PostCacheDao
import com.example.data.repository.db.PostLocalDao
import com.example.domain.interfaces.ApiRepository
import com.example.domain.interfaces.DatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideApiRepository(impl: ApiRepositoryImpl): ApiRepository

    @Binds
    @Singleton
    fun provideDbRepository(impl: DatabaseRepositoryImpl): DatabaseRepository
}