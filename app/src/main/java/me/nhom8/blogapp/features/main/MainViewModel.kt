package me.nhom8.blogapp.features.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nhom8.blogapp.core.BaseViewModel
import me.nhom8.blogapp.features.main.MainState.AuthStatus
import me.nhom8.blogapp.repositories.AuthenticationRepository
import me.nhom8.blogapp.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthenticationRepository,
) : BaseViewModel() {

    private val _state = MutableStateFlow(MainState.initial)
    val state = _state.asStateFlow()

    // Optional: thêm Flow riêng để thông báo toast
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    init {
        getLoggedInUserProfile()
    }

    private fun getLoggedInUserProfile() {
        _state.update { it.copy(authStatus = AuthStatus.LOADING) }
        viewModelScope.launch {
            try {
                val user = userRepository.getUserProfile()
                _state.update { it.copy(user = user, authStatus = AuthStatus.LOGGED_IN) }
            } catch (e: Exception) {
                _state.update { it.copy(authStatus = AuthStatus.ERROR) }
                _message.value = "Không thể lấy thông tin user"
            }
        }
    }

    fun updateUserProfile(fullName: String, email: String) {
        _state.value.user?.let { user ->
            _state.update {
                it.copy(user = user.copy(fullName = fullName, email = email))
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        // TODO: Gọi repository để đổi mật khẩu, hiện tại chỉ log
        println("OldPass: $oldPassword, NewPass: $newPassword")
    }


    fun requestLogout() {
        _state.update { it.copy(authStatus = AuthStatus.SIGNED_OUT) }
        authRepository.logout()
    }

    // Clear message sau khi UI đã show
    fun messageShown() {
        _message.value = null
    }
}

