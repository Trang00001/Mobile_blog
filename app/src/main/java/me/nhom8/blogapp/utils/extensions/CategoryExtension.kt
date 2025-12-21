package me.nhom8.blogapp.utils.extensions

import me.nhom8.blogapp.R
import me.nhom8.blogapp.models.Category

fun Category.getLocalizedName(): Int {
    return when (this) {
        Category.ALL -> R.string.all
        Category.BUSINESS -> R.string.business
        Category.TECHNOLOGY -> R.string.technology
        Category.FASHION -> R.string.fashion
        Category.TRAVEL -> R.string.travel
        Category.FOOD -> R.string.food
        Category.EDUCATION -> R.string.education
    }
}
