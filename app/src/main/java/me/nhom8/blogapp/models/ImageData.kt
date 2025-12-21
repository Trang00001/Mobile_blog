package me.nhom8.blogapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val id: String = "",
    val imageUrl: String = ""
) : Parcelable
