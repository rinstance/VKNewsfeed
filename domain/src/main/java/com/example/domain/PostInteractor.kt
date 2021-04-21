package com.example.domain

import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.interfaces.ApiRepository
import com.example.domain.models.api.*
import com.example.domain.models.db.PostLocal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class PostInteractor(
    private val apiRepository: ApiRepository,
    private val databaseRepository: DatabaseRepository
) {
    private val requestDelayMillis: Long = 400

    fun deleteAllSavedPosts() {
        databaseRepository.deleteAllSavedPosts()
    }

    suspend fun getAllSavedPosts(): Flow<List<PostLocal>> = databaseRepository.getAllPosts()

    suspend fun savePost(post: Post) {
        databaseRepository.savePost(post)
    }

    suspend fun deleteSavedPost(post: Int) = databaseRepository.deletePost(post)

    suspend fun getAuthors(posts: List<Post>) {
        posts.forEach { it.postAuthor = getAuthor(it.sourceId) }
    }

    private suspend fun getAuthor(sourceId: Int): PostAuthor {
        val authors = apiRepository.getAuthors(sourceId)
        return if (authors == null) {
            delay(requestDelayMillis)
            getAuthor(sourceId)
        } else authors[0]
    }

    suspend fun getNextNewsfeed(key: String, size: Int): Newsfeed {
        val news = apiRepository.getNextNewsfeed(key, size)
        return if (news.items == null) {
            delay(requestDelayMillis)
            getNextNewsfeed(key, size)
        } else news
    }

    suspend fun getNewsfeed(loadSize: Int): Newsfeed {
        val news= apiRepository.getNewsfeed(loadSize)
        return if (news?.items == null) {
            delay(requestDelayMillis)
            getNewsfeed(loadSize)
        } else news
    }

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

    suspend fun getPostById(posts: String): List<Post> {
        val response = apiRepository.getPostById(posts).response
        return if (response == null) {
            delay(requestDelayMillis)
            getPostById(posts)
        } else response
    }

    suspend fun getVideo(post: Post, attachment: Attachment) {
        val video = getVideoById("${attachment.video.ownerId}_${attachment.video.id}")
        if (video == null)
            post.attachments = null
        else attachment.video = video
    }

    private suspend fun getVideoById(videoId: String): Video? {
        val video = apiRepository.getVideoById(videoId)
        return if (video.response == null) {
            delay(requestDelayMillis)
            getVideoById(videoId)
        } else {
            if (video.response.items.isNotEmpty())
                return video.response.items[0]
            else return null
        }
    }
}
