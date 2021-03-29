package com.example.vknewsfeed.adapters.newsfeed

import com.example.domain.models.api.Post

interface OnItemClickListener {
    fun onItemClick(post: Post)
}