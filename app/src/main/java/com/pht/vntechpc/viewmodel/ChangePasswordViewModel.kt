package com.pht.vntechpc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.local.UserPreferences
import com.pht.vntechpc.domain.usecase.user.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val regex = Regex("^\\S{6,}$")
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState
    private var email: String? = null
    init {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) { userPreferences.getUser() }
            user.let {
                email = it?.email
            }
        }
    }

    fun updateOldPassword(oldPassword: String) {
        if (regex.matches(oldPassword)) {
            _uiState.value = _uiState.value.copy(
                oldPassword = oldPassword,
                isValidOldPassword = true,
                oldPasswordError = ""
            )
        } else if (oldPassword.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(
                oldPassword = oldPassword,
                isValidOldPassword = false,
                oldPasswordError = "Yêu cầu nhập mật khẩu cũ"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                oldPassword = oldPassword,
                isValidOldPassword = false,
                oldPasswordError = "Mật khẩu cũ phải có ít nhất 6 ký tự và không được chứa khoảng trắng"
            )
        }
    }

    fun updateNewPassword(newPassword: String) {
        if (regex.matches(newPassword)) {
            _uiState.value = _uiState.value.copy(
                newPassword = newPassword,
                isValidNewPassword = true,
                newPasswordError = ""
            )
        } else if (newPassword.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(
                newPassword = newPassword,
                isValidNewPassword = false,
                newPasswordError = "Yêu cầu nhập mật khẩu mới"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                newPassword = newPassword,
                isValidNewPassword = false,
                newPasswordError = "Mật khẩu mới phải có ít nhất 6 ký tự và không được chứa khoảng trắng"
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        val newPassword = _uiState.value.newPassword.trim()

        when {
            confirmPassword.trim().isEmpty() -> {
                _uiState.value = _uiState.value.copy(
                    confirmPassword = confirmPassword,
                    isValidConfirmPassword = false,
                    confirmPasswordError = "Yêu cầu xác nhận mật khẩu mới"
                )
            }

            !regex.matches(confirmPassword) -> {
                _uiState.value = _uiState.value.copy(
                    confirmPassword = confirmPassword,
                    isValidConfirmPassword = false,
                    confirmPasswordError = "Mật khẩu xác nhận phải có ít nhất 6 ký tự và không chứa khoảng trắng"
                )
            }

            confirmPassword.trim() != newPassword -> {
                _uiState.value = _uiState.value.copy(
                    confirmPassword = confirmPassword,
                    isValidConfirmPassword = false,
                    confirmPasswordError = "Mật khẩu xác nhận không trùng khớp"
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    confirmPassword = confirmPassword,
                    isValidConfirmPassword = true,
                    confirmPasswordError = ""
                )
            }
        }
    }

    fun toggleOldPasswordVisibility() {
        _uiState.value =
            _uiState.value.copy(isOldPasswordVisible = !_uiState.value.isOldPasswordVisible)
    }

    fun toggleNewPasswordVisibility() {
        _uiState.value =
            _uiState.value.copy(isNewPasswordVisible = !_uiState.value.isNewPasswordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value =
            _uiState.value.copy(isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible)
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = ChangePasswordState.None)
    }

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        updateOldPassword(oldPassword)
        updateNewPassword(newPassword)
        updateConfirmPassword(confirmPassword)
        if (!_uiState.value.isValidOldPassword || !_uiState.value.isValidNewPassword || !_uiState.value.isValidConfirmPassword) return
        if (email == null) {
            _uiState.value = _uiState.value.copy(
                status = ChangePasswordState.Failure("Không hợp lệ"),
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = ChangePasswordState.Loading("Đang xử lý..."))
            val result = withContext(Dispatchers.IO) {
                changePasswordUseCase("", oldPassword, newPassword, confirmPassword)
            }
            result.onSuccess {
                _uiState.value =
                    _uiState.value.copy(status = ChangePasswordState.Success(it.message))
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    status = ChangePasswordState.Failure(it.message.toString()),
                )
            }
        }
    }
}

data class ChangePasswordUiState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isOldPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isValidOldPassword: Boolean = true,
    val isValidNewPassword: Boolean = true,
    val isValidConfirmPassword: Boolean = true,
    val oldPasswordError: String = "",
    val newPasswordError: String = "",
    val confirmPasswordError: String = "",
    val status: ChangePasswordState = ChangePasswordState.None
)

sealed class ChangePasswordState {
    object None : ChangePasswordState()
    data class Loading(val message: String) : ChangePasswordState()
    data class Success(val message: String) : ChangePasswordState()
    data class Failure(val message: String) : ChangePasswordState()
}