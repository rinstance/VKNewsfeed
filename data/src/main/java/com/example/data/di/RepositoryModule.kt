package com.example.data.di

import com.example.data.network.VKApi
import com.example.data.repository.api.ApiRepositoryImpl
import com.example.data.repository.db.DatabaseRepositoryImpl
import com.example.data.repository.db.PostDao
import com.example.domain.interfaces.ApiRepository
import com.example.domain.interfaces.DatabaseRepository
import com.google.firebase.database.DatabaseReference
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
        postDao: PostDao
    ): DatabaseRepository = DatabaseRepositoryImpl(postDao)
}