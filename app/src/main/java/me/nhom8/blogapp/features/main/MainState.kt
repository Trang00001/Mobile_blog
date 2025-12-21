package me.nhom8.blogapp.features.main

import me.nhom8.blogapp.models.User

data class MainState(
    val user: User?,
    val authStatus: AuthStatus,
) {
    enum class AuthStatus {
        INITIAL,
        LOADING,
        LOGGED_IN,
        SIGNED_OUT,
        ERROR,
    }

    companion object {
        val initial =
            MainState(
                user = null,
                authStatus = AuthStatus.INITIAL,
            )
    }
}
