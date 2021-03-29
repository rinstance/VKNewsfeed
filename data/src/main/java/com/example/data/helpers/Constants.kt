package com.example.data.helpers

object Constants {
    const val BASE_METHODS_URL = "https://api.vk.com/method/"
    const val BASE_AUTH_URL = "http://oauth.vk.com/authorize/"
    const val AUTH_REDIRECT_URI = "https://oauth.vk.com/blank.html"
    const val APPLICATION_ID = 7617205
    const val SECURITY_KEY = "rSkfQOWej4TV5oueZ2sm"
    const val VERSION = "5.124"
    const val FILTERS_FOR_NEWS = "post"
    const val COUNT_FOR_NEWS = 4
    const val FILTERS_FOR_USER = "photo_50"
    const val LIKE_TYPE = "post"
    const val ATTACHMENTS_PHOTO_TYPE = "photo"
    const val ATTACHMENTS_VIDEO_TYPE = "video"
    const val DATABASE_NAME = "news"
    var TOKEN = ""
    var USER_ID: Int = 0
    const val INTENT_DETAIL_DATA = "detail_data"
    const val INTENT_LIKE_COUNT = "like_count"
    const val INTENT_USER_LIKE = "user_like"
    const val INTENT_SOURCE_ID = "intent_source_id"
    const val INTENT_POST_ID = "intent_post_id"
    const val PREFERENCE_IS_AUTH = "is_auth"
    const val PREFERENCE_TOKEN = "token_pref"
    const val PREFERENCE_USER_ID = "user_id_pref"
}