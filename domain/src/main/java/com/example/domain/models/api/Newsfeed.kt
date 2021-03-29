package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class Newsfeed(
    val items: List<Post>,
    @SerializedName("next_from")
    val nextFrom: String
)