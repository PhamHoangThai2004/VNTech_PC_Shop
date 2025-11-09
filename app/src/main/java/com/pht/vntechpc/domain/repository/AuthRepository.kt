package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.LoginResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>

    suspend fun register(email: String, password: String, name: String): Result<BaseResponse<Unit>>

    suspend fun verifyOtp(otp: String): Result<BaseResponse<Unit>>

    suspend fun forgotPassword(email: String): Result<BaseResponse<Unit>>

    suspend fun verifyResetOtp(otp: String): Result<BaseResponse<Unit>>

    suspend fun resetPassword(email: String, newPassword: String): Result<BaseResponse<Unit>>
}