package com.example.domain.interfaces

import com.example.domain.models.api.Attachment
import com.example.domain.models.api.Post
import com.example.domain.models.db.PostLocal
import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun getAllPosts(): Flow<List<PostLocal>>

    suspend fun savePost(post: Post)

    suspend fun deletePost(post: Post)

    fun deleteAllSavedPosts()
}