package me.nhom8.blogapp.data.mapper

import me.nhom8.blogapp.data.remote.model.response.blog.CategoryResponse
import me.nhom8.blogapp.data.remote.model.response.blog.CategoryEnum
import me.nhom8.blogapp.models.Category

/**
 * Map domain Category -> remote CategoryResponse để gửi lên backend.
 */
fun Category.toRemoteModel(): CategoryResponse {
    val (id, name) = when (this) {
        Category.BUSINESS -> "" to CategoryEnum.BUSINESS
        Category.TECHNOLOGY -> "" to CategoryEnum.TECHNOLOGY
        Category.FASHION -> "" to CategoryEnum.FASHION
        Category.TRAVEL -> "" to CategoryEnum.TRAVEL
        Category.FOOD -> "" to CategoryEnum.FOOD
        Category.EDUCATION -> "" to CategoryEnum.EDUCATION
        // ALL không hợp lệ khi tạo blog, mặc định map về BUSINESS
        Category.ALL -> "" to CategoryEnum.BUSINESS
    }
    return CategoryResponse(id = id, name = name)
}
