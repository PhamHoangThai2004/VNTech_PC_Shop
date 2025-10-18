package com.pht.vntechpc.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.usecase.auth.RegisterUseCase
import com.pht.vntechpc.domain.usecase.auth.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

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

    fun updateFullName(fullName: String) {
        if (fullName.trim().isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                fullName = fullName,
                isValidFullName = true,
                fullNameError = ""
            )
        } else if (fullName.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(
                fullName = fullName,
                isValidFullName = false,
                fullNameError = "Yêu cầu nhập tên"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                fullName = fullName,
                isValidFullName = true,
                fullNameError = "Tên quá dài"
            )
        }
    }

    fun updateOtp(otp: String) {
        if (otp.length == 6) {
            _uiState.value = _uiState.value.copy(
                otp = otp,
                isValidOtp = true,
                otpError = ""
            )
        } else if (otp.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                otp = otp,
                isValidOtp = false,
                otpError = "Yêu cầu nhập mã OTP"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                otp = otp,
                isValidOtp = false,
                otpError = "Mã OTP không hợp lệ"
            )
        }
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = SignupState.None)
    }

    fun goToConfirmOtp() {
        _uiState.value = _uiState.value.copy(isPage = SignupStatePage.ConfirmOtp)
    }

    fun backToEnterAccount() {
        _uiState.value = _uiState.value.copy(
            isPage = SignupStatePage.EnterAccount,
            otp = "",
            otpError = "",
            isValidOtp = true
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun register(email: String, password: String, name: String) {
        updateEmail(email)
        updatePassword(password)
        updateFullName(name)
        if (!_uiState.value.isValidEmail || !_uiState.value.isValidPassword || !_uiState.value.isValidFullName) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = SignupState.Loading)
            val result = registerUseCase(email, password, name.trim())
            result.onSuccess {
                Log.d("BBB", "Register successful: $it")
                _uiState.value =
                    _uiState.value.copy(status = SignupState.RegisterSuccess(it))
            }.onFailure {
                Log.d("BBB", "Register failed: ${it.message}")
                _uiState.value = _uiState.value.copy(
                    status = SignupState.Failure("Email đã được đăng ký trước đó"),
                )
            }
        }
    }

    fun verifyOtp(otp: String) {
        updateOtp(otp)
        if (!_uiState.value.isValidOtp) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = SignupState.Loading)
            val result = verifyOtpUseCase(otp)
            result.onSuccess {
                Log.d("BBB", "Verify OTP successful: $it")
                _uiState.value =
                    _uiState.value.copy(status = SignupState.VerifyOtpSuccess(it))
            }.onFailure {
                Log.d("BBB", "Verify OTP failed: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = SignupState.Failure("${it.message}"))
            }
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = SignupState.Loading)
            val result = registerUseCase(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.fullName
            )
            result.onSuccess {
                Log.d("BBB", "Register successful: $it")
                _uiState.value =
                    _uiState.value.copy(
                        status = SignupState.RegisterSuccess("Đã gửi lại mã OTP"),
                    )
            }.onFailure {
                Log.d("BBB", "Register failed: ${it.message}")
                _uiState.value = _uiState.value.copy(
                    status = SignupState.Failure("Email đã được đăng ký trước đó"),
                )
            }
        }
    }

}

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val otp: String = "",
    val isPasswordVisible: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidFullName: Boolean = true,
    val isValidOtp: Boolean = true,
    val emailError: String = "",
    val passwordError: String = "",
    val fullNameError: String = "",
    val otpError: String = "",
    val status: SignupState = SignupState.None,
    var isPage: SignupStatePage = SignupStatePage.EnterAccount
)

sealed class SignupStatePage() {
    object EnterAccount : SignupStatePage()
    object ConfirmOtp : SignupStatePage()
}

sealed class SignupState {
    object None : SignupState()
    object Loading : SignupState()
    data class RegisterSuccess(val message: String) : SignupState()
    data class VerifyOtpSuccess(val message: String) : SignupState()
    data class Failure(val message: String) : SignupState()
}