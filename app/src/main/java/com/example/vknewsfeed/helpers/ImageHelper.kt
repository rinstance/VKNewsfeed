package com.example.vknewsfeed.helpers

import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageHelper {
    fun downloadAndSetImage(image: ImageView, photoUrl: String) {
        Glide.with(image.context)
            .load(photoUrl)
            .into(image)
    }
}