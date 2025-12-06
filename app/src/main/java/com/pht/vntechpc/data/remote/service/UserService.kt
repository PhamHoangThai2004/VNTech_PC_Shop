package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.UserApi
import com.pht.vntechpc.data.remote.model.request.ChangePasswordRequest
import com.pht.vntechpc.data.remote.model.request.UserRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class UserService @Inject constructor(private val api: UserApi) {
    suspend fun changePassword(email: String, request: ChangePasswordRequest) =
        api.changePassword(email, request)

    suspend fun fetchUser() = api.fetchUser()

    suspend fun updateUser(email: String, request: UserRequest) =
        api.updateUser(email, request)

    suspend fun uploadAvatar(userId: Int, file: MultipartBody.Part) = api.uploadAvatar(userId, file)

    suspend fun deleteAvatar(userId: Int) = api.deleteAvatar(userId)
}