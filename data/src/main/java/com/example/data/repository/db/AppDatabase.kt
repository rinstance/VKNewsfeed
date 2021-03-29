package com.example.data.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.domain.converters.AttachmentsConverter
import com.example.domain.models.db.PostLocal

@Database(entities = [PostLocal::class], version = 1)
@TypeConverters(AttachmentsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun get(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "news.db"
                ).build()
            }
            return instance!!
        }
    }

    abstract fun postDao(): PostDao
}