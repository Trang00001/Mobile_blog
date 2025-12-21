package me.nhom8.blogapp.data.mapper

import me.nhom8.blogapp.data.remote.model.response.blog.BlogResponse
import me.nhom8.blogapp.models.Blog

fun BlogResponse.toDomainModel(): Blog {
    return Blog(
        id = id,
        title = title,
        content = content,
        imageUrl = imageUrl,
        category = category.toDomainModel(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        creator = creator.toDomainUser(),
    )
}
