package com.example.data.di

import android.content.Context
import com.example.data.repository.db.AppDatabase
import com.example.data.repository.db.PostCacheDao
import com.example.data.repository.db.PostLocalDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return AppDatabase.get(context)
    }

    @Provides
    @Singleton
    fun providePostDao(database: AppDatabase): PostLocalDao = database.postLocalDao()

    @Provides
    @Singleton
    fun providePostCacheDao(database: AppDatabase): PostCacheDao = database.postCacheDao()
}