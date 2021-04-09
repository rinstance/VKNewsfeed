package com.example.domain.models.db

import androidx.room.*
import com.example.domain.converters.AttachmentIdConverter

@Entity
data class PostLocal(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val sourceId: Int,
    val text: String,
    val date: String,
    val author: String,
    val authorUrl: String
)