package com.example.vknewsfeed.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.domain.helpers.Constants
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.auth.AuthorizationActivity
import com.example.vknewsfeed.activities.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val preference = PreferenceManager.getDefaultSharedPreferences(this)

        if (preference.getBoolean(Constants.PREFERENCE_IS_AUTH, false))
            startActivity(
                Intent(this, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        else startActivity(
            Intent(this, AuthorizationActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
    }
}
