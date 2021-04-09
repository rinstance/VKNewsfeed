package com.example.domain.converters

import androidx.room.TypeConverter

class AttachmentIdConverter {
    @TypeConverter
    fun fromList(attachment: List<Int>): String =
        attachment.joinToString(separator = ":")

    @TypeConverter
    fun toList(string: String): List<Int> =
        string.split(":").map { it.trim().toInt() }
}