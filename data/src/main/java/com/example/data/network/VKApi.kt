package com.example.data.network

import com.example.data.network.models.like.ResponseLikes
import com.example.data.network.models.newsfeed.NewsfeedResponse
import com.example.data.network.models.newsfeed.ResponseAuthor
import com.example.data.network.models.saved_post.SavePhotoResponse
import com.example.domain.models.api.SavedPost
import com.example.data.network.models.upload_post.WallResponse
import com.example.domain.models.api.WallUploadData
import com.example.data.network.models.upload_post.WallUploadResponse
import com.example.data.network.models.user.UsersResponse
import com.example.data.network.models.video.ResponseVideo
import okhttp3.MultipartBody
import retrofit2.http.*

interface VKApi {
    @GET("newsfeed.get")
    suspend fun getResponseNewsfeed(
        @Query("filters") filters: String,
        @Query("count") count: String
    ): NewsfeedResponse

    @GET("newsfeed.get")
    suspend fun getNextResponseNewsfeed(
        @Query("filters") filters: String,
        @Query("count") count: String,
        @Query("start_from") startFrom: String
    ): NewsfeedResponse

    @GET("users.get")
    suspend fun getUserAuthor(
        @Query("user_ids") id: Int,
        @Query("fields") fields: String
    ): ResponseAuthor

    @GET("groups.getById")
    suspend fun getGroupAuthor(
        @Query("group_ids") id: Int
    ): ResponseAuthor

    @GET("likes.add")
    suspend fun setLike(
        @Query("type") type: String,
        @Query("owner_id") ownerId: Int,
        @Query("item_id") itemId: Int
    ): ResponseLikes

    @GET("likes.delete")
    suspend fun deleteLike(
        @Query("type") type: String,
        @Query("owner_id") ownerId: Int,
        @Query("item_id") itemId: Int
    ): ResponseLikes

    @POST("photos.getWallUploadServer")
    suspend fun getWallUploadServer(
        @Query("group_id") groupId: Int
    ): WallUploadResponse

    @Multipart
    @POST
    suspend fun getUrl(@Url uploadUrl: String, @Part photo: MultipartBody.Part): WallUploadData

    @POST("photos.saveWallPhoto")
    suspend fun saveWallPhoto(
        @Query("user_id") userId: Int,
        @Query("group_id") groupId: Int,
        @Query("photo") photo: String,
        @Query("server") server: Int,
        @Query("hash") hash: String
    ): SavePhotoResponse

    @GET("users.get")
    suspend fun getUsers(): UsersResponse

    @POST("wall.post")
    suspend fun wallPost(
        @Query("owner_id") ownerId: Int,
        @Query("message") message: String,
        @Query("attachments") attachments: String
    ): SavedPost

    @GET("wall.getById")
    suspend fun getPostById(
        @Query("posts") posts: String
    ): WallResponse

    @GET("video.get")
    suspend fun getVideoById(
        @Query("videos") videoId: String
    ) : ResponseVideo
}
