package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.domain.model.User
import java.io.File

interface UserRepository {
    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Result<BaseResponse<Unit>>

    suspend fun fetchUser(): Result<User>

    suspend fun updateUser(
        email: String,
        fullName: String,
        gender: String,
        dateOfBirth: String,
    ): Result<BaseResponse<Unit>>

    suspend fun uploadAvatar(userId: Int, file: File): Result<User>

    suspend fun deleteAvatar(userId: Int): Result<BaseResponse<Unit>>
}