package me.nhom8.blogapp.data.mapper

import me.nhom8.blogapp.data.remote.model.response.user.UserResponse
import me.nhom8.blogapp.models.User

fun UserResponse.toDomainUser(): User {
    return User(
        id = id,
        fullName = fullName,
        email = email,
        following = following,
        follower = follower,
    )
}
