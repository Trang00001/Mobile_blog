package me.nhom8.blogapp.features.image_upload

import me.nhom8.blogapp.features.helpers.LoadingStatus

data class ImageUploadState(
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
    val imageUrl: String? = null,
    val error: Throwable? = null,
) {
    companion object {
        val initial = ImageUploadState()
    }
}
