package com.example.vknewsfeed.activities.main

import com.example.domain.MainInteractor
import com.example.domain.PostInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainPresenter(
    private val mainInteractor: MainInteractor,
    private val postInteractor: PostInteractor
): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    fun setupData(): Boolean = mainInteractor.setStartData()

    fun logout() {
        mainInteractor.setPreferenceLogout()
    }

    fun close() {
        launch(coroutineContext) { postInteractor.clearCachedPosts() }
    }
}