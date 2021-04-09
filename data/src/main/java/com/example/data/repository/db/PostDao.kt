package com.example.data.repository.db

import androidx.room.*
import com.example.domain.models.db.PostLocal
import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM postLocal")
    fun getAll(): Flow<List<PostLocal>>

    @Query("SELECT * FROM postLocal WHERE id = :id")
    fun getById(id: Int): Flow<PostLocal>

    @Query("DELETE FROM postlocal")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostLocal)

    @Update
    fun updatePost(post: PostLocal)

    @Delete
    fun deletePost(post: PostLocal)
}