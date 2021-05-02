package com.example.vknewsfeed.activities.auth

import com.example.domain.BuildConfig

object AuthConstants {
    const val accessToken = "access_token="
    const val userId = "user_id="
    const val baseAuthUrl = "https://oauth.vk.com/authorize?"
    const val clientId = "&client_id=${BuildConfig.VK_APPLICATION_ID}"
    const val scope = "&scope=friends,wall,offline,photos,video"
    const val other = "&response_type=token&v=${BuildConfig.API_VERSION}&state=123456"
}