package com.example.vknewsfeed.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.example.data.helpers.Constants
import com.example.vknewsfeed.R
import com.example.vknewsfeed.helpers.loadUrlWebView
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class AuthorizationActivity : AppCompatActivity() {
    private val accessToken = "access_token="
    private val userId = "user_id="
    private val baseAuthUrl = "https://oauth.vk.com/authorize?"
    private val clientId = "&client_id=${Constants.APPLICATION_ID}"
    private val scope = "&scope=friends,wall,offline,photos,video"
    private val other = "&response_type=token&v=${Constants.VERSION}&state=123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        setToolbar()
        loadUrlWebView(
            webview_auth,
            baseAuthUrl + clientId + scope + other,
            VKAuthWebViewClient(),
            this
        )
    }

    private fun setToolbar() {
        (toolbar_title as TextView).setText(R.string.TOOLBAR_TITLE_AUTH)
        action_back.visibility = View.GONE
    }

    inner class VKAuthWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            val tokenSplit = url.split(accessToken).toTypedArray()
            if (tokenSplit.size > 1) {
                val preferences =
                    PreferenceManager.getDefaultSharedPreferences(this@AuthorizationActivity)
                preferences.edit()
                    .putString(Constants.PREFERENCE_TOKEN, parseQueryBy(url, accessToken)).apply()
                preferences.edit()
                    .putString(Constants.PREFERENCE_USER_ID, parseQueryBy(url, userId)).apply()
                preferences.edit().putBoolean(Constants.PREFERENCE_IS_AUTH, true).apply()
                startNewsfeedActivity()
            }
        }
    }

    private fun parseQueryBy(url: String, parameter: String): String =
        url.split(parameter).toTypedArray()[1].split("&").toTypedArray()[0]

    fun startNewsfeedActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}