package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.request.UserRequest
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
        request: UserRequest
    ): Result<BaseResponse<Unit>>

    suspend fun uploadAvatar(userId: String, file: File): Result<Unit>

    suspend fun deleteAvatar(userId: String): Result<Unit>
}