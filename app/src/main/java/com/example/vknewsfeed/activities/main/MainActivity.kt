package com.example.vknewsfeed.activities.main

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.domain.helpers.Constants
import com.example.vknewsfeed.App
import com.example.vknewsfeed.R
import com.example.vknewsfeed.activities.AuthorizationActivity
import com.example.vknewsfeed.fragments.ProgressDialogFragment
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AppRouter {
    private lateinit var progressDialog: ProgressDialogFragment
    @Inject lateinit var presenter: MainPresenter

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_host) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDI()
        setupData()
        setBottomNavigation()
    }

    private fun setDI() {
        App.appComponent.mainComponentFactory()
            .create(this)
            .inject(this)
    }

    private fun setupData() {
        if(!presenter.setupData()) {
            startActivity(
                Intent(this, AuthorizationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
    }

    private fun setBottomNavigation() {
        bottom_nav_view.setupWithNavController(navController)
    }

    override fun showToast(textId: Int) =
        Toast.makeText(this, textId, Toast.LENGTH_SHORT).show()

    override fun openDetailFragment(postId: Int, sourceId: Int) {
        val bundle = Bundle().apply {
            putInt(Constants.INTENT_SOURCE_ID, sourceId)
            putInt(Constants.INTENT_POST_ID, postId)
        }
        bottom_nav_view?.visibility = View.GONE
        navController.navigate(R.id.action_to_detail_post, bundle)
    }
}
