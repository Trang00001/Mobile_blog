package me.nhom8.blogapp.data.remote.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import me.nhom8.blogapp.data.remote.model.response.blog.CategoryEnum

/**
 * Adapter để serialize/deserialize CategoryEnum.
 */
class CategoryEnumAdapter {
    @ToJson
    fun toJson(type: CategoryEnum): String = type.categoryName

    @FromJson
    fun fromJson(value: String): CategoryEnum =
        CategoryEnum.entries.firstOrNull { it.categoryName == value }
            ?: CategoryEnum.BUSINESS
}
