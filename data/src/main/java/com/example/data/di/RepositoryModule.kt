package com.example.data.di

import com.example.data.network.VKApi
import com.example.data.repository.api.ApiRepositoryImpl
import com.example.data.repository.db.DatabaseRepositoryImpl
import com.example.data.repository.db.PostCacheDao
import com.example.data.repository.db.PostLocalDao
import com.example.domain.interfaces.ApiRepository
import com.example.domain.interfaces.DatabaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideApiRepository(vkApi: VKApi): ApiRepository = ApiRepositoryImpl(vkApi)

    @Provides
    @Singleton
    fun provideDbRepository(
        postLocalDao: PostLocalDao,
        postCacheDao: PostCacheDao
    ): DatabaseRepository = DatabaseRepositoryImpl(postLocalDao, postCacheDao)
}