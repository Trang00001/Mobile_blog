package me.nhom8.blogapp.features.new_blog

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nhom8.blogapp.core.BaseViewModel
import me.nhom8.blogapp.data.mapper.toRemoteModel
import me.nhom8.blogapp.data.remote.model.body.blog.UpsertBlogBody
import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.models.Blog
import me.nhom8.blogapp.models.Category
import me.nhom8.blogapp.repositories.BlogRepository
import javax.inject.Inject

@HiltViewModel
class NewBlogViewModel
    @Inject
    constructor(
        private val blogRepository: BlogRepository,
    ) : BaseViewModel() {

        private val _state = MutableStateFlow(NewBlogState.initial)
        val state = _state.asStateFlow()

        fun setEditingBlog(blog: Blog) {
            _state.update {
                it.copy(
                    mode = NewBlogState.Mode.EDIT,
                    blogId = blog.id,
                    title = blog.title,
                    content = blog.content,
                    imageUrl = blog.imageUrl,
                    category = blog.category,
                    submitStatus = LoadingStatus.INITIAL,
                    error = null,
                )
            }
        }

        fun setTitle(title: String) {
            _state.update { it.copy(title = title) }
        }

        fun setContent(content: String) {
            _state.update { it.copy(content = content) }
        }

        fun setCategory(category: Category) {
            _state.update { it.copy(category = category) }
        }

        fun setImageUrl(url: String?) {
            _state.update { it.copy(imageUrl = url) }
        }

        fun submit() {
            val current = _state.value
            _state.update { it.copy(submitStatus = LoadingStatus.LOADING, error = null) }

            viewModelScope.launch {
                try {
                    val body =
                        UpsertBlogBody(
                            title = current.title,
                            content = current.content,
                            imageUrl = current.imageUrl ?: "",
                            category = current.category.toRemoteModel(),
                        )

                    when (current.mode) {
                        NewBlogState.Mode.CREATE -> {
                            blogRepository.createBlog(body)
                        }

                        NewBlogState.Mode.EDIT -> {
                            val blogId = current.blogId ?: throw IllegalStateException("Missing blogId")
                            blogRepository.updateBlog(blogId, body)
                        }
                    }

                    _state.update { it.copy(submitStatus = LoadingStatus.DONE) }
                } catch (e: Exception) {
                    _state.update { it.copy(submitStatus = LoadingStatus.ERROR, error = e) }
                }
            }
        }

        fun resetSubmitStatus() {
            _state.update { it.copy(submitStatus = LoadingStatus.INITIAL, error = null) }
        }
    }
