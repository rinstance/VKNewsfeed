package com.example.data.repository.db

import com.example.data.mappers.mapPostCacheToPost
import com.example.data.mappers.mapPostToPostCache
import com.example.data.mappers.mapPostToPostLocal
import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.models.api.Post
import com.example.domain.models.db.PostLocal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val postLocalDao: PostLocalDao,
    private val postCacheDao: PostCacheDao
) : DatabaseRepository {

    override suspend fun getAllPosts(userId: Int): Flow<List<PostLocal>> =
        postLocalDao.getAll(userId)

    override suspend fun savePost(post: Post, userId: Int?) {
//        userId?.let {
//            FirebaseDatabase.getInstance().getReference("posts").setValue(mapPostToPostLocal(post, userId))
//        }
        userId?.let {
            postLocalDao.insertPost(mapPostToPostLocal(post, userId))
        }
    }

    override suspend fun deletePost(postId: Int) = postLocalDao.deletePostById(postId)

    override suspend fun cachePost(post: Post) {
        postCacheDao.insertPost(mapPostToPostCache(post))
    }

    override suspend fun getCachePosts(): List<Post> {
        val posts = postCacheDao.getAll()
        return if (posts.isEmpty())
            ArrayList()
        else posts.map { mapPostCacheToPost(it) }
    }

    override suspend fun clearCachePosts() {
        postCacheDao.deleteAll()
    }

    override fun deleteAllSavedPosts() = postLocalDao.deleteAll()

}