package com.example.data.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.models.db.cache.PostCache

@Dao
interface PostCacheDao {
    @Query("SELECT * FROM PostCache ORDER BY date DESC")
    fun getAll(): List<PostCache>

    @Query("DELETE FROM PostCache")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostCache)
}