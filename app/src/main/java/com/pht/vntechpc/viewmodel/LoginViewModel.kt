package com.pht.vntechpc.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.local.UserPreferences
import com.pht.vntechpc.domain.usecase.auth.LoginUseCase
import com.pht.vntechpc.domain.usecase.user.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun updateEmail(email: String) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(
                email = email,
                isValidEmail = true,
                emailError = ""
            )
        } else if (email.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                email = email,
                isValidEmail = false,
                emailError = "Yêu cầu nhập email"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                email = email,
                isValidEmail = false,
                emailError = "Email không hợp lệ"
            )
        }
    }

    fun updatePassword(password: String) {
        val regex = Regex("^\\S{6,}$")
        if (regex.matches(password)) {
            _uiState.value = _uiState.value.copy(
                password = password,
                isValidPassword = true,
                passwordError = ""
            )
        } else if (password.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(
                password = password,
                isValidPassword = false,
                passwordError = "Yêu cầu nhập mật khẩu"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                password = password,
                isValidPassword = false,
                passwordError = "Mật khẩu phải có ít nhất 6 ký tự và không được chứa khoảng trắng"
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = LoginState.None)
    }

    fun login(email: String, password: String) {
//        _uiState.value = _uiState.value.copy(status = LoginState.Success)
        updateEmail(email)
        updatePassword(password)
        if (!_uiState.value.isValidEmail || !_uiState.value.isValidPassword) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoginState.Loading)
            val result = loginUseCase(email, password)
            result.onSuccess {
                if (it.role == "USER") {
                    Log.d("BBB", "Login success: ${it.accessToken}")
                    userPreferences.saveToken(it.accessToken)
                    getProfile()
                } else {
                    _uiState.value = _uiState.value.copy(
                        status = LoginState.Failure("Bạn không có quyền truy cập"),
                    )
                }
            }.onFailure {
                Log.d("BBB", "Login failure: ${it.message}")
                _uiState.value = _uiState.value.copy(
                    status = LoginState.Failure(it.message.toString()),
                )
            }
        }
    }

    private suspend fun getProfile() {
        val result = getProfileUseCase()
        result.onSuccess {
            val user = it
            Log.d("BBB", "User: $user")
            userPreferences.saveUser(user)
            _uiState.value = _uiState.value.copy(status = LoginState.Success)
        }.onFailure {
            _uiState.value = _uiState.value.copy(
                status = LoginState.Failure(it.message.toString()),
            )
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val emailError: String = "",
    val passwordError: String = "",
    val status: LoginState = LoginState.None
)

sealed class LoginState {
    object None : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Failure(val message: String) : LoginState()
}