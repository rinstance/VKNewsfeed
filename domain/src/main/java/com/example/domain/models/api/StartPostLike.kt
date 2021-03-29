package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class StartPostLike(
    var count: Int,
    @SerializedName("user_likes")
    var userLikes: Int
)