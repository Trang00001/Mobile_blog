package me.nhom8.blogapp.features.main.home

import me.nhom8.blogapp.features.helpers.LoadingStatus
import me.nhom8.blogapp.features.main.home.ui_model.HomePageBlog
import me.nhom8.blogapp.models.Blog
import me.nhom8.blogapp.models.Category
import me.nhom8.blogapp.utils.AppConstants

data class HomeState(
    val blogs: List<Blog>,
    val homePageBlogs: List<HomePageBlog>,
    val selectedCategory: Category,
    val loadFirstPageStatus: LoadingStatus,
    val loadMoreStatus: LoadingStatus,
    val currentPage: Int,
) {
    companion object {
        val initial =
            HomeState(
                homePageBlogs = emptyList(),
                blogs = emptyList(),
                selectedCategory = Category.ALL,
                loadFirstPageStatus = LoadingStatus.INITIAL,
                loadMoreStatus = LoadingStatus.INITIAL,
                currentPage = AppConstants.START_BLOG_QUERY_PAGE,
            )
    }
}
