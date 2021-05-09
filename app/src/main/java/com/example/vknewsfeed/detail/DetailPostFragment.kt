package com.example.vknewsfeed.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.data.helpers.*
import com.example.domain.helpers.Constants
import com.example.domain.models.api.Attachment
import com.example.domain.models.api.Post
import com.example.domain.models.api.StartPostLike
import com.example.vknewsfeed.App
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.main.MainActivity
import com.example.vknewsfeed.newsfeed.adapters.grid.GridImagesAdapter
import com.example.vknewsfeed.newsfeed.adapters.grid.GridVideosAdapter
import com.example.vknewsfeed.helpers.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_detail_post.*
import java.util.ArrayList
import javax.inject.Inject

class DetailPostFragment : Fragment() {
    private val imageHelper = ImageHelper()
    private lateinit var post: Post
    @Inject lateinit var model: DetailViewModel

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = close()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_detail_post, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
        setDI()
        getDetailPost()
        setLikeListener()
    }

    private fun setDI() {
        App.appComponent
            .detailComponentFactory()
            .create(this, activity as MainActivity)
            .inject(this)
    }

    private fun setLikeListener() {
        detail_like_text?.setOnClickListener { model.doLike(post) }
        model.mutableItemAfterLike.observe(viewLifecycleOwner, Observer { setLikeView(it.likes) })
    }
    
    private fun setView(post: Post) {
        post.getAuthorPostName().let {
            toolbar_title.text = it
            detail_author_name.text = it
        }
        layout_loading?.setLoading(false)
        detail_text?.text = post.text
        detail_date?.text = getFormatDate(post.date)
        setLikeView(post.likes)
        setAdapters(post.attachments)
        imageHelper.downloadAndSetImage(detail_author_image, post.postAuthor.photo50)
        action_back?.setOnClickListener { onBackPressed() }
    }

    private fun setLikeView(itemLike: StartPostLike) {
        detail_like_text.apply {
            text = itemLike.count.toString()
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.like_image_states, 0)
            compoundDrawables[2].level = itemLike.userLikes
        }
    }

    private fun getDetailPost() {
        val postId = arguments?.getInt(Constants.INTENT_POST_ID, 0)
        val sourceId = arguments?.getInt(Constants.INTENT_SOURCE_ID, 0)
        model.mutableDetailPost.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                post = it
                setView(it)
            }
        })
        model.setupPost(postId, sourceId)
    }

    private fun setAdapters(attachments: List<Attachment>?) {
        val photoAttachments: MutableList<String> = ArrayList()
        val videoAttachments: MutableList<String> = ArrayList()
        if (attachments != null) {
            attachments.forEach {
                if (it.type == Constants.ATTACHMENTS_PHOTO_TYPE)
                    photoAttachments.add(it.photo.getMaxSizeUrl())
                if (it.type == Constants.ATTACHMENTS_VIDEO_TYPE)
                    videoAttachments.add(it.video.url)
            }
            gridview_images.adapter = GridImagesAdapter(photoAttachments)
            gridview_videos.adapter = GridVideosAdapter(videoAttachments)
        }
    }

    private fun onBackPressed() = requireActivity().onBackPressed()

    private fun close() {
        Bundle().apply {
            putInt(Constants.INTENT_POST_ID, post.postId)
            putInt(Constants.INTENT_LIKE_COUNT, post.likes.count)
            putInt(Constants.INTENT_USER_LIKE, post.likes.userLikes)
            setNavigationResult(this, Constants.INTENT_DETAIL_DATA)
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        model.setPostNull()
        super.onDestroyView()
    }
}
