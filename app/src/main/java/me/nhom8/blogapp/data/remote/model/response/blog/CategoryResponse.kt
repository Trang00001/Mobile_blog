package me.nhom8.blogapp.data.remote.model.response.blog

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: CategoryEnum,
)

enum class CategoryEnum(val categoryName: String) {
    @Json(name = "business")
    BUSINESS("business"),
    @Json(name = "technology")
    TECHNOLOGY("technology"),
    @Json(name = "fashion")
    FASHION("fashion"),
    @Json(name = "travel")
    TRAVEL("travel"),
    @Json(name = "food")
    FOOD("food"),
    @Json(name = "education")
    EDUCATION("education"),
}
