package me.nhom8.blogapp.data.mapper

import me.nhom8.blogapp.data.remote.model.response.blog.CategoryResponse
import me.nhom8.blogapp.data.remote.model.response.blog.CategoryEnum
import me.nhom8.blogapp.models.Category

fun CategoryResponse.toDomainModel(): Category {
    return when (this.name) {
        CategoryEnum.BUSINESS -> Category.BUSINESS
        CategoryEnum.TECHNOLOGY -> Category.TECHNOLOGY
        CategoryEnum.FASHION -> Category.FASHION
        CategoryEnum.TRAVEL -> Category.TRAVEL
        CategoryEnum.FOOD -> Category.FOOD
        CategoryEnum.EDUCATION -> Category.EDUCATION
    }
}
