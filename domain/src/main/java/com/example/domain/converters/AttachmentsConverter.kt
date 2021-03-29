package com.example.domain.converters

import androidx.room.TypeConverter
import com.example.domain.models.db.AttachmentLocal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AttachmentsConverter {
    @TypeConverter
    fun fromAttachments(attachments: List<AttachmentLocal>): String = Gson().toJson(attachments)

    @TypeConverter
    fun toAttachments(attachments: String): List<AttachmentLocal> {
        val listType: Type = object : TypeToken<List<AttachmentLocal>>() {}.type
        return Gson().fromJson(attachments, listType)
    }
}