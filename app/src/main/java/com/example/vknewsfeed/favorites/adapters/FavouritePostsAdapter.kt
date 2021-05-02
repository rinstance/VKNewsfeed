package com.example.vknewsfeed.favorites.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.db.PostLocal
import com.example.vknewsfeed.R
import com.example.vknewsfeed.helpers.ImageHelper
import kotlinx.android.synthetic.main.newsfeed_item.view.*

class FavouritePostsAdapter(
    private val itemClick: (PostLocal) -> Unit,
    private val longClick: (PostLocal) -> Unit
) : ListAdapter<PostLocal, FavouritePostsAdapter.FavouritePostsViewHolder>(PostLocalDiffUtilCallback()) {
    private val imageHelper = ImageHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritePostsViewHolder =
        FavouritePostsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.newsfeed_item, parent, false)
        )

    override fun onBindViewHolder(holder: FavouritePostsViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick, longClick)
    }

    override fun submitList(list: List<PostLocal>?) {
        super.submitList(if (list != null) ArrayList(list) else list)
    }

    inner class FavouritePostsViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(post: PostLocal, itemClick: (PostLocal) -> Unit, longClick: (PostLocal) -> Unit) {
            with(itemView) {
                like_count_text.visibility = View.GONE
                like_button.visibility = View.GONE
                author_name.text = post.author
                date.text = post.date
                text.text = post.text
                imageHelper.downloadAndSetImage(author_image, post.authorUrl)
                setOnClickListener { itemClick(post) }
                setOnLongClickListener {
                    longClick(post)
                    true
                }
            }
        }
    }

    class PostLocalDiffUtilCallback : DiffUtil.ItemCallback<PostLocal>() {
        override fun areItemsTheSame(oldItem: PostLocal, newItem: PostLocal): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PostLocal, newItem: PostLocal): Boolean =
            oldItem.date == newItem.date
    }

}
