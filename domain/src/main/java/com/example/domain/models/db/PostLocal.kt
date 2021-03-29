package com.example.domain.models.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.domain.converters.AttachmentsConverter

@Entity
data class PostLocal(
    @PrimaryKey
    var id: Int,
    val text: String,
    val date: String,
    val author: String,
    @TypeConverters(AttachmentsConverter::class)
    var attachments: List<AttachmentLocal>?
)