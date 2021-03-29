package com.example.domain.converters

import androidx.room.TypeConverter

class AttachmentTypeConverter {
    @TypeConverter
    fun fromList(attachment: List<String>): String =
        attachment.joinToString(separator = ":")

    @TypeConverter
    fun toList(string: String): List<String> =
        string.split(":").map { it.trim() }
}