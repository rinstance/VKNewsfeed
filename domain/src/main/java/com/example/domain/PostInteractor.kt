package com.example.domain

import android.content.SharedPreferences
import android.util.Log
import com.example.domain.helpers.Constants
import com.example.domain.interfaces.DatabaseRepository
import com.example.domain.interfaces.ApiRepository
import com.example.domain.models.api.*
import com.example.domain.models.api.video.ResponseVideo
import com.example.domain.models.db.PostLocal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import kotlin.collections.ArrayList

const val requestDelayMillis: Long = 400

class PostInteractor(
    private val apiRepository: ApiRepository,
    private val databaseRepository: DatabaseRepository,
    private val preference: SharedPreferences
) {

    fun deleteAllSavedPosts() {
        databaseRepository.deleteAllSavedPosts()
    }

    suspend fun getAllSavedPosts(): Flow<List<PostLocal>> {
        return databaseRepository.getAllPosts(Constants.USER_ID)
    }

    suspend fun savePost(post: Post) {
        databaseRepository.savePost(post, Constants.USER_ID)
    }

    suspend fun deleteSavedPost(post: Int) = databaseRepository.deletePost(post)

    suspend fun getAuthors(posts: List<Post>) {
        posts.forEach { it.postAuthor = getAuthor(it.sourceId) }
    }

    private suspend fun getAuthor(sourceId: Int): PostAuthor {
        val authors: List<PostAuthor>? = apiRepository.getAuthors(sourceId)
        return if (authors == null) {
            delay(requestDelayMillis)
            getAuthor(sourceId)
        } else authors[0]
    }

    suspend fun getNextNewsfeed(key: String, size: Int): Newsfeed {
        val news: Newsfeed? = apiRepository.getNextNewsfeed(key, size)
        return if (news?.posts == null) {
            delay(requestDelayMillis)
            getNextNewsfeed(key, size)
        } else {
            getNewsData(news)
            findByFilters(news)
        }
    }

    suspend fun getNewsfeed(loadSize: Int): Newsfeed {
        val news: Newsfeed? = apiRepository.getNewsfeed(loadSize)
        return if (news?.posts == null) {
            delay(requestDelayMillis)
            getNewsfeed(loadSize)
        } else {
            getNewsData(news)
            findByFilters(news)
        }
    }

    private suspend fun getNewsData(news: Newsfeed) {
        getAuthors(news.posts)
        getVideosForPosts(news.posts)
    }

    private suspend fun findByFilters(news: Newsfeed): Newsfeed {
        val filterType =
            preference.getString(Constants.PREF_CONTENT_FILTER, Constants.ATTACHMENTS_ALL_TYPE)
        val posts: MutableList<Post> = ArrayList()
        if (filterType != Constants.ATTACHMENTS_ALL_TYPE) {
            news.posts.asFlow().filter { post ->
                val flag = post.attachments?.any { it.type == filterType }
                flag != null && flag
            }.collect {
                posts.add(it)
            }
            news.posts = posts
        }
        return news
    }

    private suspend fun cachePosts(posts: List<Post>) {
        posts.forEach { databaseRepository.cachePost(it) }
    }

    private suspend fun getWallUpload(): WallUploadModel = apiRepository.getWallUpload()

    private suspend fun getWallUploadData(
        uploadUrl: String,
        photo: MultipartBody.Part
    ): WallUploadData =
        apiRepository.getWallUploadData(uploadUrl, photo)

    private suspend fun saveWallPhoto(
        photo: String,
        hash: String,
        server: Int,
        userId: Int
    ): SavePhotoModel = apiRepository.saveWallPhoto(photo, hash, server, userId)

    private suspend fun wallPost(message: String, attachments: String): SavedPost =
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
        val video: ResponseVideo? = apiRepository.getVideoById(videoId)
        return if (video?.response == null) {
            delay(requestDelayMillis)
            getVideoById(videoId)
        } else {
            return if (video.response.items.isNotEmpty())
                video.response.items[0]
            else null
        }
    }

    suspend fun createPost(photo: MultipartBody.Part?, message: String): SavedPost {
        return if (photo != null) {
            val wallUploadResponse = getWallUpload()
            val wallUploadData = getWallUploadData(wallUploadResponse.uploadUrl, photo)
            val savePhotoModel = saveWallPhoto(
                wallUploadData.photo,
                wallUploadData.hash,
                wallUploadData.server,
                wallUploadResponse.userId
            )
            wallPost(message, "photo${Constants.USER_ID}_${savePhotoModel.id}")
        } else {
            wallPost(message, "")
        }
    }

    private suspend fun getVideosForPosts(posts: List<Post>) {
        posts.forEach { post ->
            post.attachments?.forEach {
                if (it.type == Constants.ATTACHMENTS_VIDEO_TYPE) {
                    getVideo(post, it)
                }
            }
        }
    }

    suspend fun clearCachedPosts() {
        databaseRepository.clearCachePosts()
    }

    fun setFilterType(type: String) {
        preference.edit().putString(Constants.PREF_CONTENT_FILTER, type).apply()
    }
}
