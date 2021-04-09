package com.example.vknewsfeed.newsfeed.adapters.newsfeed

import com.example.domain.models.api.Post

interface ILikeListener {
    fun likeAction(post: Post)
}