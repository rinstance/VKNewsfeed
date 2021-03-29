package com.example.vknewsfeed.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.data.helpers.Constants
import com.example.vknewsfeed.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupData()
        setBottomNavigation()
    }

    private fun setupData() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val prefToken = preferences.getString(Constants.PREFERENCE_TOKEN, "")
        val prefUserId = preferences.getString(Constants.PREFERENCE_USER_ID, "")
        if (hasTokenAndUserId(prefToken, prefUserId)) {
            prefToken?.let { Constants.TOKEN = it }
            prefUserId?.let { Constants.USER_ID = it.toInt() }
        } else {
            startActivity(
                Intent(this, AuthorizationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
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

    private fun setBottomNavigation() {
//        supportFragmentManager.beginTransaction().add(R.id.frame, NewsfeedFragment()).commit()
        navController = findNavController(R.id.fragment_host)
        bottom_nav_view.setupWithNavController(navController)
    }
}
