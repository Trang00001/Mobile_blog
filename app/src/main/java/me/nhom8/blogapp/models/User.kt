package me.nhom8.blogapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val following: Int,
    val follower: Int,
) : Parcelable
