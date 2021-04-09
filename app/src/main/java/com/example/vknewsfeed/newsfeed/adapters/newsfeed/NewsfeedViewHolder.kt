package com.example.vknewsfeed.newsfeed.adapters.newsfeed

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.api.Post
import com.example.vknewsfeed.helpers.ImageHelper
import com.example.data.helpers.getAuthorPostName
import com.example.data.helpers.getFormatDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.newsfeed_item.*

class NewsfeedViewHolder(
    override val containerView: View,
    private val itemClick: (Post) -> Unit,
    private val likeAction: (Post) -> Unit,
    private val longClick: (Post) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private val imageHelper: ImageHelper = ImageHelper()

    fun bind(item: Post) {
        setBody(item)
        setAuthor(item)
        setLike(item)
        setItemClickListeners(item)
    }

    private fun setLike(item: Post) {
        like_count_text.text = item.likes.count.toString()
        like_button.setImageLevel(item.likes.userLikes)
    }

    private fun setAuthor(item: Post) {
        val author = item.postAuthor
        author_name.text = item.getAuthorPostName()
        imageHelper.downloadAndSetImage(author_image, author.photo50)
    }

    private fun setBody(item: Post) {
        text.text = item.text
        date.text = getFormatDate(item.date)
    }

    private fun setItemClickListeners(post: Post) {
        like_button.setOnClickListener { likeAction(post) }
        itemView.setOnClickListener { itemClick(post) }
        itemView.setOnLongClickListener {
            longClick(post)
            true
        }
    }
}
