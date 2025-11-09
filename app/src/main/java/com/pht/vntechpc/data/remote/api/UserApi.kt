package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.request.ChangePasswordRequest
import com.pht.vntechpc.data.remote.model.request.UserRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("v1/user/change-password")
    suspend fun changePassword(
        @Query("email") email: String,
        @Body request: ChangePasswordRequest
    ): Response<BaseResponse<Unit>>

    @GET("v1/user/profile")
    suspend fun fetchUser(): Response<BaseResponse<UserResponse>>

    @PUT("v1/user/profile")
    suspend fun updateUser(
        @Query("email") email: String,
        @Body request: UserRequest
    ): Response<BaseResponse<Unit>>

    @Multipart
    @POST("v1/user/profile/{id}/avatar")
    suspend fun uploadAvatar(
        @Path("id") userId: String,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @DELETE("v1/user/profile/{id}/avatar")
    suspend fun deleteAvatar(@Path("id") userId: String): Response<Unit>

}