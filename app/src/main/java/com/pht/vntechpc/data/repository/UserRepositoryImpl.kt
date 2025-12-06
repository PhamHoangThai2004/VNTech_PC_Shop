package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.request.ChangePasswordRequest
import com.pht.vntechpc.data.remote.model.request.UserRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.toUser
import com.pht.vntechpc.data.remote.service.UserService
import com.pht.vntechpc.domain.model.User
import com.pht.vntechpc.domain.repository.BaseRepository
import com.pht.vntechpc.domain.repository.UserRepository
import jakarta.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepositoryImpl @Inject constructor(
    private val service: UserService
) : BaseRepository(), UserRepository {
    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Result<BaseResponse<Unit>> {
        val request = ChangePasswordRequest(oldPassword, newPassword, confirmNewPassword)
        return apiCallNoData { service.changePassword(email, request) }
    }

    override suspend fun fetchUser(): Result<User> {
        return apiCall({ service.fetchUser() }, { it.toUser() })
    }

    override suspend fun updateUser(
        email: String,
        fullName: String,
        gender: String,
        dateOfBirth: String,
    ): Result<BaseResponse<Unit>> {
        val request = UserRequest(fullName, fullName, gender, dateOfBirth, "")
        return apiCallNoData { service.updateUser(email, request) }
    }

    override suspend fun uploadAvatar(
        userId: Int,
        file: File
    ): Result<User> {
        val request = file.asRequestBody("image/*".toMediaType())
        val filePart = MultipartBody.Part.createFormData("file", file.name, request)
        return apiCall({ service.uploadAvatar(userId, filePart) }) { it.toUser() }
    }

    override suspend fun deleteAvatar(userId: Int): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.deleteAvatar(userId) }
    }
}