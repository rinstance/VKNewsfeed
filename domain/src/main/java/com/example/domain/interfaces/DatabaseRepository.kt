package com.example.domain.interfaces

import com.example.domain.models.api.Post
import com.example.domain.models.db.PostLocal
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun getAllPosts(userId: Int): Flow<List<PostLocal>>

    suspend fun savePost(post: Post, userId: Int?)

    suspend fun deletePost(postId: Int)

    fun deleteAllSavedPosts()

}
