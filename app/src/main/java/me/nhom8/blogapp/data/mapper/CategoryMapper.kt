package me.nhom8.blogapp.data.mapper

import me.nhom8.blogapp.data.remote.model.response.blog.CategoryResponse
import me.nhom8.blogapp.models.Category

fun CategoryResponse.toDomainModel(): Category {
    return when (this) {
        CategoryResponse.BUSINESS -> Category.BUSINESS
        CategoryResponse.TECHNOLOGY -> Category.TECHNOLOGY
        CategoryResponse.FASHION -> Category.FASHION
        CategoryResponse.TRAVEL -> Category.TRAVEL
        CategoryResponse.FOOD -> Category.FOOD
        CategoryResponse.EDUCATION -> Category.EDUCATION
    }
}
