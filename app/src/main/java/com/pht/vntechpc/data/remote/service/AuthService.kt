package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.AuthApi
import com.pht.vntechpc.data.remote.model.request.ChangePasswordRequest
import com.pht.vntechpc.data.remote.model.request.LoginRequest
import com.pht.vntechpc.data.remote.model.request.RegisterRequest
import javax.inject.Inject

class AuthService @Inject constructor(private val api: AuthApi) {
    suspend fun login(request: LoginRequest): String {
        val response = api.login(request)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }

    suspend fun register(request: RegisterRequest): String {
        val response = api.register(request)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }

    suspend fun verifyOtp(otp: String): String {
        val response = api.verifyOtp(otp)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }

    suspend fun forgotPassword(email: String): String {
        val response = api.forgotPassword(email)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }

    suspend fun verifyResetOtp(otp: String): String {
        val response = api.verifyResetOtp(otp)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }

    suspend fun resetPassword(email: String, newPassword: String): String {
        val response = api.resetPassword(email, newPassword)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }

    suspend fun changePassword(email: String, request: ChangePasswordRequest): String {
        val response = api.changePassword(email, request)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
        return response.body()?.string() ?: ""
    }
}