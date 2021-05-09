package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class Newsfeed(
    @SerializedName("items")
    var posts: List<Post>,
    @SerializedName("next_from")
    val nextFrom: String?
)