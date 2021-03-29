package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class PostAuthor(
    val id: Int,
    @SerializedName("photo_50")
    val photo50: String,
    // for users
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    // for groups
    val name: String
)