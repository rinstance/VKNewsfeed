package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.helpers.Constants
import com.example.data.repository.db.AppDatabase
import com.example.data.repository.db.PostDao
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
    fun provideUserDao(database: AppDatabase): PostDao = database.postDao()
}