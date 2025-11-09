package com.pht.vntechpc.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.usecase.auth.ForgotPasswordUseCase
import com.pht.vntechpc.domain.usecase.auth.ResetPasswordUseCase
import com.pht.vntechpc.domain.usecase.auth.VerifyResetOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ForgetViewModel @Inject constructor(
    private val forgetPasswordUseCase: ForgotPasswordUseCase,
    private val verifyResetOtpUseCase: VerifyResetOtpUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ForgetUiState())
    val uiState: StateFlow<ForgetUiState> = _uiState

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

    fun updatePassword(newPassword: String) {
        val regex = Regex("^\\S{6,}$")
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
                newPasswordError = "Yêu cầu nhập mật khẩu"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                newPassword = newPassword,
                isValidNewPassword = false,
                newPasswordError = "Mật khẩu phải có ít nhất 6 ký tự và không được chứa khoảng trắng"
            )
        }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.value =
            _uiState.value.copy(isNewPasswordVisible = !_uiState.value.isNewPasswordVisible)
    }


    fun goToConfirmOtp() {
        _uiState.value = _uiState.value.copy(isPage = ForgetStatePage.ConfirmOtp)
    }

    fun goToEnterNewPassword() {
        _uiState.value = _uiState.value.copy(isPage = ForgetStatePage.EnterNewPassword)
    }

    fun backToEnterEmail() {
        _uiState.value = _uiState.value.copy(
            isPage = ForgetStatePage.EnterEmail,
            otp = "",
            otpError = "",
            isValidOtp = true
        )
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = ForgetState.None)
    }

    fun forgotPassword(email: String) {
        updateEmail(email)
        if (!_uiState.value.isValidEmail) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = ForgetState.Loading)
            val result = forgetPasswordUseCase(email)
            result.onSuccess {
                Log.d("ForgetViewModel", "Password reset email sent successfully")
                _uiState.value =
                    _uiState.value.copy(status = ForgetState.ForgetPasswordSuccess(it.message))

            }.onFailure {
                Log.d("ForgetViewModel", "Password reset email sent failed: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = ForgetState.Failure(it.message.toString()))
            }
        }
    }

    fun verifyResetOtp(otp: String) {
        updateOtp(otp)
        if (!_uiState.value.isValidOtp) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = ForgetState.Loading)
            val result = verifyResetOtpUseCase(otp)
            result.onSuccess {
                Log.d("ForgetViewModel", "OTP verified successfully")
                _uiState.value =
                    _uiState.value.copy(status = ForgetState.VerifyResetOtpSuccess(it.message))
            }.onFailure {
                Log.d("ForgetViewModel", "OTP verification failed: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = ForgetState.Failure(it.message.toString()))
            }
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        updatePassword(newPassword)
        if (!_uiState.value.isValidNewPassword) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = ForgetState.Loading)
            val result = resetPasswordUseCase(email, newPassword)
            result.onSuccess {
                Log.d("ForgetViewModel", "Password reset successfully")
                _uiState.value =
                    _uiState.value.copy(status = ForgetState.ResetPasswordSuccess(it.message))

            }.onFailure {
                Log.d("ForgetViewModel", "Password reset failed: ${it.message}")
            }
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = ForgetState.Loading)
            val result = forgetPasswordUseCase(_uiState.value.email)
            result.onSuccess {
                Log.d("BBB", "Resend email successful: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(
                        status = ForgetState.ForgetPasswordSuccess("Đã gửi lại mã OTP"),
                    )
            }.onFailure {
                Log.d("BBB", "Resend email failed: ${it.message}")
                _uiState.value = _uiState.value.copy(
                    status = ForgetState.Failure(it.message.toString()),
                )
            }
        }
    }
}

data class ForgetUiState(
    val email: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val isNewPasswordVisible: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidOtp: Boolean = true,
    val isValidNewPassword: Boolean = true,
    val emailError: String = "",
    val otpError: String = "",
    val newPasswordError: String = "",
    val status: ForgetState = ForgetState.None,
    val isPage: ForgetStatePage = ForgetStatePage.EnterEmail
)

sealed class ForgetStatePage() {
    object EnterEmail : ForgetStatePage()
    object ConfirmOtp : ForgetStatePage()
    object EnterNewPassword : ForgetStatePage()
}

sealed class ForgetState() {
    object None : ForgetState()
    object Loading : ForgetState()
    data class ForgetPasswordSuccess(val message: String) : ForgetState()
    data class VerifyResetOtpSuccess(val message: String) : ForgetState()
    data class ResetPasswordSuccess(val message: String) : ForgetState()
    data class Failure(val message: String) : ForgetState()
}