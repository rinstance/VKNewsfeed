package com.example.data.repository.db

import com.example.data.mappers.mapPostToPostLocal
import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.models.api.Post
import com.example.domain.models.db.PostLocal
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(
    private val postDao: PostDao
) : DatabaseRepository {

    override suspend fun getAllPosts(userId: Int): Flow<List<PostLocal>> = postDao.getAll(userId)

    override suspend fun savePost(post: Post, userId: Int?) {
        userId?.let {
            postDao.insertPost(mapPostToPostLocal(post, userId))
        }
    }

    override suspend fun deletePost(postId: Int) = postDao.deletePostById(postId)

    override fun deleteAllSavedPosts() = postDao.deleteAll()

}