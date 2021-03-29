package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class Attachment(
    val photo: Photo,
    var video: Video,
    val type: String
)

data class Video(
    val id: Int,
    @SerializedName("player")
    val url: String,
    @SerializedName("owner_id")
    val ownerId: Int
)

data class Photo(
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val sizes: List<Size>
)

data class Size(
    val height: Int,
    val url: String,
    val width: Int
)
