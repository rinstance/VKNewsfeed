package com.example.data.di

import android.content.Context
import com.example.data.repository.db.AppDatabase
import com.example.data.repository.db.PostDao
import com.example.domain.helpers.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    fun providePostDao(database: AppDatabase): PostDao = database.postDao()

}