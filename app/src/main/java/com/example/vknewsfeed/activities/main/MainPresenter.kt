package com.example.vknewsfeed.activities.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.domain.MainInteractor

class MainPresenter(
    private val mainInteractor: MainInteractor
) {
    fun setupData(): Boolean = mainInteractor.setStartData()
}