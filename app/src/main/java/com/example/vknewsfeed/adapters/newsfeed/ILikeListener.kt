package com.example.vknewsfeed.adapters.newsfeed

import com.example.domain.models.api.Post

interface ILikeListener {
    fun likeAction(post: Post)
}