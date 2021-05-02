package com.example.domain

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.domain.helpers.Constants

class MainInteractor(
    private val preferences: SharedPreferences
) {
    fun setStartData(): Boolean {
        val prefToken = preferences.getString(Constants.PREFERENCE_TOKEN, "")
        val prefUserId = preferences.getString(Constants.PREFERENCE_USER_ID, "")
        if (hasTokenAndUserId(prefToken, prefUserId)) {
            prefToken?.let { Constants.TOKEN = it }
            prefUserId?.let { Constants.USER_ID = it.toInt() }
            return true
        }
        return false
    }

    private fun hasTokenAndUserId(token: String?, userId: String?): Boolean {
        val tokenOrNull = token.takeIf {
            (it != null) && it.isNotEmpty()
        }
        val userIdOrNull = userId.takeIf {
            (it != null) && it.isNotEmpty()
        }
        return tokenOrNull != null && userIdOrNull != null
    }

}