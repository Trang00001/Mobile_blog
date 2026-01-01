package me.nhom8.blogapp.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.nhom8.blogapp.data.mapper.toDomainModel
import me.nhom8.blogapp.data.remote.ApiService
import me.nhom8.blogapp.data.remote.model.body.blog.UpsertBlogBody
import me.nhom8.blogapp.di.IoDispatcher
import me.nhom8.blogapp.models.Blog
import me.nhom8.blogapp.utils.AppConstants
import timber.log.Timber
import javax.inject.Inject

class BlogRepository
    @Inject
    constructor(
        private val apiService: ApiService,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) {
        suspend fun getBlogs(
            limit: Int = AppConstants.BLOGS_QUERY_LIMIT,
            page: Int,
        ): List<Blog> {
            return withContext(ioDispatcher) {
                apiService
                    .getBlogs(limit = limit, page = page)
                    .unwrap()
                    .map { it.toDomainModel() }
                    .also { Timber.d("GetBlogs { page = $page, limit = $limit }") }
            }
        }

        suspend fun createBlog(body: UpsertBlogBody): Blog {
            return withContext(ioDispatcher) {
                apiService
                    .createBlog(body)
                    .unwrap()
                    .toDomainModel()
                    .also { Timber.d("CreateBlog(title=${body.title})") }
            }
        }

        suspend fun updateBlog(
            blogId: String,
            body: UpsertBlogBody,
        ): Blog {
            return withContext(ioDispatcher) {
                apiService
                    .updateBlog(id = blogId, body = body)
                    .unwrap()
                    .toDomainModel()
                    .also { Timber.d("UpdateBlog(id=$blogId)") }
            }
        }

        suspend fun deleteBlog(blogId: String) {
            return withContext(ioDispatcher) {
                apiService
                    .deleteBlog(id = blogId)
                    .unwrap()
                    .also { Timber.d("DeleteBlog(id=$blogId)") }
            }
        }
    }
