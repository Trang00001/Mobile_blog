package me.nhom8.blogapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.squareup.moshi.Json


@Parcelize
data class User(
    val id: String,

    val fullName: String,
    val email: String,

    val avatarUrl: String?,
    val following: Int,

    val follower: Int
) : Parcelable
