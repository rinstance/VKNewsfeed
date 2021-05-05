package com.example.vknewsfeed.routers

interface AppRouter {
    fun showToast(textId: Int)
    fun openDetailFragment(postId: Int, sourceId: Int)
    fun openAuthActivity()
    fun showProgressBar()
    fun hideProgressBar()
}