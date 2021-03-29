package com.example.vknewsfeed.favorites

import com.example.domain.PostInteractor
import com.example.vknewsfeed.MainViewModel

class FavouritesViewModel(
    private val postInteractor: PostInteractor
) : MainViewModel(postInteractor) {

}