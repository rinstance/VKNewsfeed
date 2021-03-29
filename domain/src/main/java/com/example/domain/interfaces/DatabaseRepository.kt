package com.example.domain.interfaces

import com.example.domain.models.api.Post
import com.example.domain.models.db.PostLocal
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun getAllPosts(): List<PostLocal>

    suspend fun savePost(post: Post)

    suspend fun deletePost(post: Post)
}