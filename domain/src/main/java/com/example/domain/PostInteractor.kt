package com.example.domain

import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.interfaces.ApiRepository
import com.example.domain.models.api.*
import com.example.domain.models.db.PostLocal
import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class PostInteractor(
    private val apiRepository: ApiRepository,
    private val databaseRepository: DatabaseRepository
) {

    fun deleteAllSavedPosts() {
        databaseRepository.deleteAllSavedPosts()
    }

    suspend fun getAllSavedPosts(): Flow<List<PostLocal>> = databaseRepository.getAllPosts()

    suspend fun savePost(post: Post) {
        databaseRepository.savePost(post)
//        post.attachments?.forEach {
//            databaseRepository.saveAttachment(it, post.postId)
//        }
    }

    suspend fun deleteSavedPost(post: Post) = databaseRepository.deletePost(post)

    suspend fun getAuthors(posts: List<Post>) {
        posts.forEach { it.postAuthor = apiRepository.getAuthors(it.sourceId) }
    }

    suspend fun getNextNewsfeed(key: String, size: Int): Newsfeed =
        apiRepository.getNextNewsfeed(key, size)

    suspend fun getNewsfeed(loadSize: Int): Newsfeed = apiRepository.getNewsfeed(loadSize)

    suspend fun getWallUpload(): WallUploadModel = apiRepository.getWallUpload()

    suspend fun getWallUploadData(uploadUrl: String, photo: MultipartBody.Part): WallUploadData =
        apiRepository.getWallUploadData(uploadUrl, photo)

    suspend fun saveWallPhoto(
        photo: String,
        hash: String,
        server: Int,
        userId: Int
    ): SavePhotoModel = apiRepository.saveWallPhoto(photo, hash, server, userId)

    suspend fun wallPost(message: String, attachments: String): SavedPost =
        apiRepository.wallPost(message, attachments)

    suspend fun setLike(post: Post): Int = apiRepository.setLike(post)

    suspend fun deleteLike(post: Post): Int = apiRepository.deleteLike(post)

    suspend fun getPostById(posts: String): List<Post> = apiRepository.getPostById(posts)

    suspend fun getVideo(post: Post, attachment: Attachment) {
        val video = apiRepository.getVideoById("${attachment.video.ownerId}_${attachment.video.id}")
        if (video == null)
            post.attachments = null
        else attachment.video = video
    }
}
