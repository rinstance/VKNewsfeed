package com.example.vknewsfeed.helpers

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.vknewsfeed.R

class ImageHelper {
    fun downloadAndSetImage(image: ImageView, photoUrl: String) {
        Glide.with(image.context)
            .load(photoUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(image)
    }
}