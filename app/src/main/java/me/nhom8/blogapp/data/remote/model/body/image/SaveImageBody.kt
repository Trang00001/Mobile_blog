package me.nhom8.blogapp.data.remote.model.body.image

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveImageBody(
    @Json(name = "imageUrl")
    val imageUrl: String
)
