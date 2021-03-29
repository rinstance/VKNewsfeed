package com.example.vknewsfeed.adapters.newsfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.example.domain.models.api.Post
import com.example.vknewsfeed.R

class NewsfeedAdapter(
    diffUtilCallback: ItemDiffUtilCallback,
    private val itemClick: (Post) -> Unit,
    private val likeAction: (Post) -> Unit
) : PagedListAdapter<Post, NewsfeedViewHolder>(diffUtilCallback) {
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var iLikeListener: ILikeListener

    fun setDoLikeListener(iLikeListener: ILikeListener) {
        this.iLikeListener = iLikeListener
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsfeedViewHolder =
            NewsfeedViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.newsfeed_item, parent, false),
                    itemClick, likeAction)

    override fun onBindViewHolder(holder: NewsfeedViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}