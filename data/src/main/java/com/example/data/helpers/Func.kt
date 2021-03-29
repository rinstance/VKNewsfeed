package com.example.data.helpers

import com.example.domain.models.api.Photo
import com.example.domain.models.api.Post
import java.text.SimpleDateFormat
import java.util.*

fun getFormatDate(date: Int): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(date * 1000L))

fun Post.getAuthorPostName(): String =
    if (sourceId < 0) postAuthor.name else postAuthor.firstName + " " + postAuthor.lastName

fun Photo.getMaxSizeUrl(): String = sizes[sizes.size - 1].url
