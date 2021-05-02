package com.example.vknewsfeed.activities.auth

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.example.domain.BuildConfig
import com.example.domain.helpers.Constants
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.main.MainActivity
import com.example.vknewsfeed.helpers.loadUrlWebView
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class AuthorizationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        setToolbar()
        loadUrlWebView(
            webview_auth,
            AuthConstants.baseAuthUrl + AuthConstants.clientId + AuthConstants.scope + AuthConstants.other,
            VKAuthWebViewClient(),
            this
        )
    }

    private fun setToolbar() {
        (toolbar_title as TextView).setText(R.string.authorization)
        action_back.visibility = View.GONE
    }

    inner class VKAuthWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            val tokenSplit = url.split(AuthConstants.accessToken).toTypedArray()
            if (tokenSplit.size > 1) {
                val preferences =
                    PreferenceManager.getDefaultSharedPreferences(this@AuthorizationActivity)
                preferences.edit()
                    .putString(Constants.PREFERENCE_TOKEN, parseQueryBy(url, AuthConstants.accessToken)).apply()
                preferences.edit()
                    .putString(Constants.PREFERENCE_USER_ID, parseQueryBy(url, AuthConstants.userId)).apply()
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