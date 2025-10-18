package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.request.ChangePasswordRequest
import com.pht.vntechpc.data.remote.model.request.LoginRequest
import com.pht.vntechpc.data.remote.model.request.RegisterRequest
import com.pht.vntechpc.data.remote.service.AuthService
import com.pht.vntechpc.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val request = LoginRequest(email, password)
            val result = authService.login(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<String> {
        return try {
            val request = RegisterRequest(email, password, name)
            val result = authService.register(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(otp: String): Result<String> {
        return try {
            val result = authService.verifyOtp(otp)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return try {
            val result = authService.forgotPassword(email)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyResetOtp(otp: String): Result<String> {
        return try {
            val result = authService.verifyResetOtp(otp)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String, newPassword: String): Result<String> {
        return try {
            val result = authService.resetPassword(email, newPassword)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): Result<String> {
        return try {
            val request = ChangePasswordRequest(oldPassword, newPassword)
            val result = authService.changePassword(email, request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}