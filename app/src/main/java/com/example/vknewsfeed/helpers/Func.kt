package com.example.vknewsfeed.helpers

import android.content.Context
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(key)

fun Fragment.setNavigationResult(result: Bundle, key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun loadUrlWebView(webView: WebView, url: String, wevClient: WebViewClient, context: Context) {
    webView.settings.javaScriptEnabled = true
    webView.clearCache(true)
    webView.webViewClient = wevClient
    CookieSyncManager.createInstance(context)
    val cookieManager = CookieManager.getInstance()
    cookieManager.removeAllCookie()
    webView.loadUrl(url)
}