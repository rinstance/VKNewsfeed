package com.example.domain.helpers

object Constants {
    const val FILTERS_FOR_NEWS = "post"
    const val COUNT_FOR_NEWS = 12
    const val FILTERS_FOR_USER = "photo_50"
    const val LIKE_TYPE = "post"
    const val IMAGE_TYPE_INTENT = "image/*"
    const val ATTACHMENTS_PHOTO_TYPE = "photo"
    const val ATTACHMENTS_VIDEO_TYPE = "video"
    const val ATTACHMENTS_ALL_TYPE = "all"
    var TOKEN = ""
    var USER_ID: Int = 0

    //region intent data
    const val INTENT_DETAIL_DATA = "detail_data"
    const val INTENT_LIKE_COUNT = "like_count"
    const val INTENT_USER_LIKE = "user_like"
    const val INTENT_SOURCE_ID = "intent_source_id"
    const val INTENT_POST_ID = "intent_post_id"
    //endregion

    //region preference
    const val PREFERENCE_IS_AUTH = "is_auth"
    const val PREFERENCE_TOKEN = "token_pref"
    const val PREFERENCE_USER_ID = "user_id_pref"
    const val PREF_CONTENT_FILTER = "pref_content_filter"
    //endregion
}