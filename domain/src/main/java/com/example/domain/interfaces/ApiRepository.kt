package com.example.domain.interfaces

import com.example.domain.models.api.*
import com.example.domain.models.api.upload_post.WallResponse
import com.example.domain.models.api.video.ResponseVideo
import okhttp3.MultipartBody

interface ApiRepository {

    suspend fun setLike(post: Post): Int

    suspend fun deleteLike(post: Post): Int

    suspend fun getNewsfeed(requestedLoadSize: Int): Newsfeed

    suspend fun getNextNewsfeed(key: String, requestedLoadSize: Int): Newsfeed

    suspend fun getAuthors(sourceId: Int): List<PostAuthor>

    suspend fun getGroupAuthor(sourceId: Int): List<PostAuthor>

    suspend fun getWallUpload(): WallUploadModel

    suspend fun getWallUploadData(uploadUrl: String, photo: MultipartBody.Part): WallUploadData

    suspend fun saveWallPhoto(photo: String, hash: String, server: Int, userId: Int): SavePhotoModel

    suspend fun getCurrentUser(): User

    suspend fun wallPost(message: String, attachments: String): SavedPost

    suspend fun getPostById(posts: String): WallResponse

    suspend fun getVideoById(videoId: String): ResponseVideo

}
