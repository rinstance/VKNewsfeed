package com.example.domain.models.api

import com.google.gson.annotations.SerializedName

data class Post(
    val attachments: List<Attachment>?,
    val date: Int,
    val likes: StartPostLike,
    @SerializedName("post_id")
    var postId: Int,
    @SerializedName("source_id", alternate = ["owner_id"])
    val sourceId: Int,
    val text: String,
    @Transient
    var postAuthor: PostAuthor
)