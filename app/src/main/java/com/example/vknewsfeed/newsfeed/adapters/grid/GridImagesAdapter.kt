package com.example.vknewsfeed.newsfeed.adapters.grid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.vknewsfeed.R
import com.example.vknewsfeed.helpers.ImageHelper

class GridImagesAdapter(private val urls: List<String>) : BaseAdapter() {
    private val imageHelper = ImageHelper()

    override fun getCount(): Int = urls.size

    override fun getItem(position: Int): Any = urls[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.gridview_image_item, null);
        }
        val image = view?.findViewById<ImageView>(R.id.gridview_image_item)
        if (image != null)
            imageHelper.downloadAndSetImage(image, urls[position])
        return view
    }
}
