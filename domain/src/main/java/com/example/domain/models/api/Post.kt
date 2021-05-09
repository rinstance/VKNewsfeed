package com.example.domain.models.api

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Post(
    var attachments: List<Attachment>?,
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