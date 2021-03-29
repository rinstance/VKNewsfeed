package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean,
    @SerializedName("first_name")
    val firstName: String,
    val id: Int,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_50")
    val photo50: String
)
