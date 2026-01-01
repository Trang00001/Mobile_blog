package me.nhom8.blogapp.features.blog_detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nhom8.blogapp.core.BaseViewModel
import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.repositories.BlogRepository
import javax.inject.Inject

@HiltViewModel
class BlogDetailViewModel
    @Inject
    constructor(
        private val blogRepository: BlogRepository,
    ) : BaseViewModel() {

        private val _state = MutableStateFlow(BlogDetailState.initial)
        val state = _state.asStateFlow()

        fun deleteBlog(blogId: String) {
            _state.update { it.copy(deleteStatus = LoadingStatus.LOADING, error = null) }
            viewModelScope.launch {
                try {
                    blogRepository.deleteBlog(blogId)
                    _state.update { it.copy(deleteStatus = LoadingStatus.DONE) }
                } catch (e: Exception) {
                    _state.update { it.copy(deleteStatus = LoadingStatus.ERROR, error = e) }
                }
            }
        }

        fun resetDeleteStatus() {
            _state.update { it.copy(deleteStatus = LoadingStatus.INITIAL, error = null) }
        }
    }
