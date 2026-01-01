package me.nhom8.blogapp.features.new_blog

import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.models.Category

data class NewBlogState(
    val mode: Mode,
    val blogId: String?,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val category: Category,
    val submitStatus: LoadingStatus,
    val error: Throwable?,
) {
    enum class Mode {
        CREATE,
        EDIT,
    }

    companion object {
        val initial =
            NewBlogState(
                mode = Mode.CREATE,
                blogId = null,
                title = "",
                content = "",
                imageUrl = null,
                category = Category.BUSINESS,
                submitStatus = LoadingStatus.INITIAL,
                error = null,
            )
    }
}
