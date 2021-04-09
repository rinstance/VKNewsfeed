package com.example.vknewsfeed.newsfeed

import androidx.lifecycle.viewModelScope
import com.example.data.helpers.Constants
import com.example.domain.PostInteractor
import com.example.domain.models.api.Post
import com.example.domain.models.api.SavedPost
import com.example.vknewsfeed.MainViewModel
import com.example.vknewsfeed.R
import com.example.vknewsfeed.routers.AppRouter
import kotlinx.coroutines.*
import okhttp3.MultipartBody

class NewsfeedViewModel(
    private val postInteractor: PostInteractor,
    private val router: AppRouter
) : MainViewModel(postInteractor) {

    suspend fun createPost(photo: MultipartBody.Part?, message: String): SavedPost {
        return withContext(Dispatchers.IO) {
            if (photo != null) {
                val wallUploadResponse = postInteractor.getWallUpload()
                val wallUploadData =
                    postInteractor.getWallUploadData(wallUploadResponse.uploadUrl, photo)
                val savePhotoModel = postInteractor.saveWallPhoto(
                    wallUploadData.photo,
                    wallUploadData.hash,
                    wallUploadData.server,
                    wallUploadResponse.userId
                )
                postInteractor.wallPost(message, "photo${Constants.USER_ID}_${savePhotoModel.id}")
            } else {
                postInteractor.wallPost(message, "")
            }
        }
    }

    fun savePost(post: Post) {
        viewModelScope.launch(coroutineContext) {
            postInteractor.savePost(post)
            withContext(Dispatchers.Main) { router.showToast(R.string.SUCCESSFUL) }
        }
    }
}