package me.nhom8.blogapp.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.nhom8.blogapp.data.mapper.toDomainModel
import me.nhom8.blogapp.data.remote.ApiService
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
                    .also { Timber.d("GetBlogs { page = $page, limit = $limit } ") }
            }
        }
    }
