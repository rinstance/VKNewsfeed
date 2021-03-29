package com.example.vknewsfeed.adapters.newsfeed

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.api.Post

class ItemDiffUtilCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.postId == newItem.postId

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.likes.count == newItem.likes.count
}
