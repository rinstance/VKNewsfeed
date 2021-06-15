package com.example.domain.models.db

import androidx.room.*

@Entity
data class PostLocal(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var userId: Int,
    val sourceId: Int,
    val text: String,
    val date: String,
    val author: String,
    val authorUrl: String
)