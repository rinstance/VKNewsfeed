package com.example.domain.models.db.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.models.api.StartPostLike
import com.example.domain.models.db.cache.PostAuthorLocal

@Entity
data class PostCache(
    @PrimaryKey(autoGenerate = true)
    var postId: Int,
    val date: Int,
    @Embedded
    val likes: StartPostLike,
    val sourceId: Int,
    val text: String,
    @Embedded
    var postAuthor: PostAuthorLocal
)