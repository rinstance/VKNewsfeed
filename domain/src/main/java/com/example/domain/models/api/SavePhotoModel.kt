package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class SavePhotoModel(
    @SerializedName("access_key")
    val accessKey: String,
    @SerializedName("album_id")
    val albumId: Int,
    val date: Int,
    @SerializedName("has_tags")
    val hasTags: Boolean,
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val text: String
)
