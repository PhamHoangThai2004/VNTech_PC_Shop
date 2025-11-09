package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.AuthApi
import com.pht.vntechpc.data.remote.model.request.LoginRequest
import com.pht.vntechpc.data.remote.model.request.RegisterRequest
import javax.inject.Inject

class AuthService @Inject constructor(private val api: AuthApi) {
    suspend fun login(request: LoginRequest) = api.login(request)

    suspend fun register(request: RegisterRequest) = api.register(request)

    suspend fun verifyOtp(otp: String) = api.verifyOtp(otp)

    suspend fun forgotPassword(email: String) = api.forgotPassword(email)

    suspend fun verifyResetOtp(otp: String) = api.verifyResetOtp(otp)

    suspend fun resetPassword(email: String, newPassword: String) =
        api.resetPassword(email, newPassword)
}