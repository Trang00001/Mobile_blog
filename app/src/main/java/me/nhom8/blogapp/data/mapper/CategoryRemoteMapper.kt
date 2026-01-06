package me.nhom8.blogapp.data.mapper

import me.nhom8.blogapp.data.remote.model.response.blog.CategoryResponse
import me.nhom8.blogapp.models.Category

/**
 * Map domain Category -> remote CategoryResponse để gửi lên backend.
 */
fun Category.toRemoteModel(): CategoryResponse {
    return when (this) {
        Category.BUSINESS -> CategoryResponse.BUSINESS
        Category.TECHNOLOGY -> CategoryResponse.TECHNOLOGY
        Category.FASHION -> CategoryResponse.FASHION
        Category.TRAVEL -> CategoryResponse.TRAVEL
        Category.FOOD -> CategoryResponse.FOOD
        Category.EDUCATION -> CategoryResponse.EDUCATION
        // ALL không hợp lệ khi tạo blog, mặc định map về BUSINESS
        Category.ALL -> CategoryResponse.BUSINESS
    }
}
