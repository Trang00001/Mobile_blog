package me.nhom8.blogapp.data.remote.model.response.image

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveImageResponse(
    @Json(name = "image_url")
    val imageUrl: String,
)
