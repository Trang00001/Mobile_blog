package me.nhom8.blogapp.features.blog_detail

import me.nhom8.blogapp.features.helpers.LoadingStatus

data class BlogDetailState(
    val deleteStatus: LoadingStatus,
    val error: Throwable?,
) {
    companion object {
        val initial = BlogDetailState(
            deleteStatus = LoadingStatus.INITIAL,
            error = null,
        )
    }
}
