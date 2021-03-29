package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class WallUploadModel(
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("upload_url")
    val uploadUrl: String,
    @SerializedName("user_id")
    val userId: Int
)