package com.example.data.repository.db

import androidx.room.*
import com.example.domain.models.db.PostLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM postLocal")
    fun getAll(): List<PostLocal>

    @Query("SELECT * FROM postLocal WHERE id = :id")
    fun getById(id: Int): PostLocal

    @Insert
    fun insert(post: PostLocal)

    @Update
    fun update(post: PostLocal)

    @Delete
    fun delete(post: PostLocal)
}