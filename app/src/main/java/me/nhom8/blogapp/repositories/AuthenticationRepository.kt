package me.nhom8.blogapp.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.nhom8.blogapp.data.local.LocalUserDataSource
import me.nhom8.blogapp.data.remote.ApiService
import me.nhom8.blogapp.data.remote.model.body.auth.LoginUserBody
import me.nhom8.blogapp.data.remote.model.body.auth.RegisterUserBody
import me.nhom8.blogapp.di.IoDispatcher
import javax.inject.Inject

class AuthenticationRepository
    @Inject
    constructor(
        private val apiService: ApiService,
        private val localDataSource: LocalUserDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) {
        suspend fun login(
            email: String,
            password: String,
        ) {
            return withContext(ioDispatcher) {
                apiService.login(
                    body =
                        LoginUserBody(
                            email = email,
                            password = password,
                        ),
                ).unwrap().also { data ->
                    localDataSource.run {
                        jwt = data.token
                        userId = data.id
                    }
                }
            }
        }

        suspend fun register(
            fullName: String,
            email: String,
            password: String,
            confirmationPassword: String,
        ) {
            return withContext(ioDispatcher) {
                apiService.register(
                    body =
                        RegisterUserBody(
                            email = email,
                            password = password,
                            confirmationPassword = confirmationPassword,
                            fullName = fullName,
                        ),
                )
            }
        }

        fun checkAuth(): Boolean {
            return localDataSource.jwt != null && localDataSource.userId != null
        }

        fun logout() = localDataSource.deleteAll()
    }
