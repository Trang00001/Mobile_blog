package me.nhom8.blogapp.features.image_upload

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nhom8.blogapp.core.BaseViewModel
import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.repositories.ImageRepository
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel
@Inject
constructor(
    private val imageRepository: ImageRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow(ImageUploadState.initial)
    val state = _state.asStateFlow()

    fun uploadImage(filePath: String) {
        Log.d("ImageUploadViewModel", "uploadImage called with path: $filePath")
        _state.update { it.copy(loadingStatus = LoadingStatus.LOADING, error = null) }

        viewModelScope.launch {
            try {
                val imageUrl = imageRepository.uploadImage(filePath)
                Log.d("ImageUploadViewModel", "Upload success: $imageUrl")

                _state.update {
                    it.copy(
                        loadingStatus = LoadingStatus.DONE,
                        imageUrl = imageUrl
                    )
                }
            } catch (e: Exception) {
                Log.e("ImageUploadViewModel", "Upload failed: ${e.message}", e)
                _state.update { it.copy(loadingStatus = LoadingStatus.ERROR, error = e) }
            }
        }
    }

    fun errorMessageShown() {
        _state.update { it.copy(loadingStatus = LoadingStatus.INITIAL, error = null) }
    }
}
