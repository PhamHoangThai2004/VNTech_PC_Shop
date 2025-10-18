package com.pht.vntechpc.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>

    suspend fun register(email: String, password: String, name: String): Result<String>

    suspend fun verifyOtp(otp: String): Result<String>

    suspend fun forgotPassword(email: String): Result<String>

    suspend fun verifyResetOtp(otp: String): Result<String>

    suspend fun resetPassword(email: String, newPassword: String): Result<String>

    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): Result<String>
}