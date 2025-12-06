package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.request.LoginRequest
import com.pht.vntechpc.data.remote.model.request.RefreshTokenRequest
import com.pht.vntechpc.data.remote.model.request.RegisterRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.AuthResponse
import com.pht.vntechpc.data.remote.service.AuthService
import com.pht.vntechpc.domain.repository.AuthRepository
import com.pht.vntechpc.domain.repository.BaseRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val service: AuthService) :
    BaseRepository(), AuthRepository {
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        val request = LoginRequest(email, password)
        return apiCallRaw { service.login(request) }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<BaseResponse<Unit>> {
        val request = RegisterRequest(email, password, name)
        return apiCallNoData { service.register(request) }
    }

    override suspend fun verifyOtp(otp: String): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.verifyOtp(otp) }
    }

    override suspend fun forgotPassword(email: String): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.forgotPassword(email) }
    }

    override suspend fun verifyResetOtp(otp: String): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.verifyResetOtp(otp) }
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.resetPassword(email, newPassword) }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        val refreshToken = RefreshTokenRequest(refreshToken)
        return apiCallRaw { service.refreshToken(refreshToken) }
    }
}