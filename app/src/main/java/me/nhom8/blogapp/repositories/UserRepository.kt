package me.nhom8.blogapp.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.nhom8.blogapp.data.local.LocalUserDataSource
import me.nhom8.blogapp.data.mapper.toDomainUser
import me.nhom8.blogapp.data.remote.ApiService
import me.nhom8.blogapp.di.IoDispatcher
import me.nhom8.blogapp.models.User
import timber.log.Timber
import javax.inject.Inject

class UserRepository
    @Inject
    constructor(
        private val apiService: ApiService,
        private val localUserDataSource: LocalUserDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) {
        suspend fun getUserProfile(): User {
            val userId = localUserDataSource.userId ?: ""
            return getUserById(id = userId)
        }

        suspend fun getUserById(id: String): User {
            return withContext(ioDispatcher) {
                apiService
                    .getUserById(id)
                    .unwrap()
                    .toDomainUser()
                    .also { Timber.d("GetUserById(id=$id)") }
            }
        }
    }
