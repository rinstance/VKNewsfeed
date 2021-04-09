package com.example.vknewsfeed.newsfeed.adapters.grid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.BaseAdapter
import com.example.vknewsfeed.R
import com.example.vknewsfeed.helpers.loadUrlWebView

class GridVideosAdapter(private val urls: List<String>) : BaseAdapter() {

    override fun getCount(): Int = urls.size

    override fun getItem(position: Int): Any = urls[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            val inflater =
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_video_item, null);
        }
        val video = view?.findViewById<WebView>(R.id.web_video)
        if (video != null && view?.context != null) {
            loadUrlWebView(
                video,
                urls[position],
                WebViewClient(),
                view.context
            )
        }
        return view
    }
}
