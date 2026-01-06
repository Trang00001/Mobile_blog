package me.nhom8.blogapp.data.remote.model.body.blog

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import me.nhom8.blogapp.data.remote.model.response.blog.CategoryResponse

@JsonClass(generateAdapter = true)
data class UpsertBlogBody(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "image_url") val imageUrl: String,
    @Json(name = "category") val category: CategoryResponse,
)
