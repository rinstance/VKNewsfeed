package com.example.data.repository.db

import com.example.data.mappers.mapPostToPostLocal
import com.example.domain.models.db.PostLocal
import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.models.api.Attachment
import com.example.domain.models.api.Post
import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(
    private val postDao: PostDao
) : DatabaseRepository {

    override suspend fun getAllPosts(): Flow<List<PostLocal>> = postDao.getAll()

    override suspend fun savePost(post: Post) = postDao.insertPost(mapPostToPostLocal(post))

    override suspend fun deletePost(postId: Int) = postDao.deletePostById(postId)

    override fun deleteAllSavedPosts() = postDao.deleteAll()
}