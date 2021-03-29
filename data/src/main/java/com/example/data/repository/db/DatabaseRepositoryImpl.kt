package com.example.data.repository.db

import com.example.data.mappers.mapPostToPostLocal
import com.example.domain.models.db.PostLocal
import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.models.api.Post
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(
    private val postDao: PostDao
) : DatabaseRepository {

    override suspend fun getAllPosts(): List<PostLocal> = postDao.getAll()

    override suspend fun savePost(post: Post) = postDao.insert(mapPostToPostLocal(post))

    override suspend fun deletePost(post: Post) = postDao.delete(mapPostToPostLocal(post))

}