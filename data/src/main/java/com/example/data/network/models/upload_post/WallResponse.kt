package com.example.data.network.models.upload_post

import com.example.domain.models.api.Post

data class WallResponse(
    val response: List<Post>?
)