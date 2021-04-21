package com.example.domain.models.api.video

import com.example.domain.models.api.Video

data class ResponseVideo(
    val response: VideoItems
)

data class VideoItems(
    val items: List<Video>
)
