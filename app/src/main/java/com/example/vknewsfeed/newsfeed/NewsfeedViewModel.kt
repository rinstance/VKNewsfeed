package com.example.vknewsfeed.newsfeed

import androidx.lifecycle.viewModelScope
import com.example.data.helpers.Constants
import com.example.domain.PostInteractor
import com.example.domain.models.api.SavedPost
import com.example.vknewsfeed.MainViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.MultipartBody

class NewsfeedViewModel(
    private val postInteractor: PostInteractor
) : MainViewModel(postInteractor) {

    suspend fun createPost(photo: MultipartBody.Part?, message: String): Deferred<SavedPost> {
        return viewModelScope.async(Dispatchers.IO) {
            if (photo != null) {
                val wallUploadResponse = postInteractor.getWallUpload()
                val wallUploadData = postInteractor.getWallUploadData(wallUploadResponse.uploadUrl, photo)
                val savePhotoModel = postInteractor.saveWallPhoto(
                    wallUploadData.photo, wallUploadData.hash, wallUploadData.server, wallUploadResponse.userId)
                postInteractor.wallPost(message, "photo${Constants.USER_ID}_${savePhotoModel.id}")
            } else {
                postInteractor.wallPost(message, "")
            }
        }
    }
}