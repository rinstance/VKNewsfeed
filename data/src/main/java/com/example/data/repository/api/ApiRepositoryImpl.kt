package com.example.data.repository.api

import com.example.data.helpers.Constants
import com.example.data.network.VKApi
import com.example.domain.models.api.upload_post.WallResponse
import com.example.domain.models.api.video.ResponseVideo
import com.example.domain.interfaces.ApiRepository
import com.example.domain.models.api.*
import okhttp3.MultipartBody

class ApiRepositoryImpl(
    val vkApi: VKApi
) : ApiRepository {

    override suspend fun setLike(post: Post) =
        vkApi.setLike(
            type = Constants.LIKE_TYPE,
            ownerId = post.sourceId,
            itemId = post.postId
        ).response.likes

    override suspend fun deleteLike(post: Post) =
        vkApi.deleteLike(
            type = Constants.LIKE_TYPE,
            ownerId = post.sourceId,
            itemId = post.postId
        ).response.likes

    private suspend fun getVideoByIdRequest(videoId: String): ResponseVideo =
        vkApi.getVideoById(videoId)

    override suspend fun getVideoById(videoId: String): ResponseVideo =
        vkApi.getVideoById(videoId)

    override suspend fun getNewsfeed(requestedLoadSize: Int): Newsfeed? =
        vkApi.getResponseNewsfeed(
            filters = Constants.FILTERS_FOR_NEWS,
            count = requestedLoadSize.toString()
        ).response

    override suspend fun getNextNewsfeed(key: String, requestedLoadSize: Int): Newsfeed =
        vkApi.getNextResponseNewsfeed(
            filters = Constants.FILTERS_FOR_NEWS,
            count = requestedLoadSize.toString(),
            startFrom = key
        ).response

    override suspend fun getAuthors(sourceId: Int): List<PostAuthor> =
        if (sourceId < 0) getGroupAuthor(sourceId) else getUserAuthor(sourceId)

    private suspend fun getUserAuthor(sourceId: Int): List<PostAuthor> =
        vkApi.getUserAuthor(
            id = sourceId,
            fields = Constants.FILTERS_FOR_USER
        ).response

    override suspend fun getGroupAuthor(sourceId: Int): List<PostAuthor> =
        vkApi.getGroupAuthor(id = sourceId * -1).response

    override suspend fun getWallUpload(): WallUploadModel =
        vkApi.getWallUploadServer(
            groupId = Constants.USER_ID
        ).response

    override suspend fun getWallUploadData(
        uploadUrl: String,
        photo: MultipartBody.Part
    ): WallUploadData =
        vkApi.getUrl(uploadUrl, photo)

    override suspend fun saveWallPhoto(
        photo: String,
        hash: String,
        server: Int,
        userId: Int
    ): SavePhotoModel =
        vkApi.saveWallPhoto(
            userId = userId,
            groupId = userId,
            photo = photo,
            server = server,
            hash = hash
        ).response[0]

    override suspend fun getCurrentUser() = vkApi.getUsers().response[0]

    override suspend fun wallPost(message: String, attachments: String) =
        vkApi.wallPost(
            ownerId = Constants.USER_ID,
            message = message,
            attachments = attachments
        )

    override suspend fun getPostById(posts: String): WallResponse = vkApi.getPostById(posts)
}