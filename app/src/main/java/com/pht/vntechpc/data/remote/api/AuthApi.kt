package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.request.ChangePasswordRequest
import com.pht.vntechpc.data.remote.model.request.LoginRequest
import com.pht.vntechpc.data.remote.model.request.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("v1/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @POST("v1/register")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseBody>

    @POST("v1/verify-otp")
    suspend fun verifyOtp(@Query("otp") otp: String): Response<ResponseBody>

    @POST("v1/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): Response<ResponseBody>

    @POST("v1/verify-reset-otp")
    suspend fun verifyResetOtp(@Query("otp") otp: String): Response<ResponseBody>

    @POST("v1/reset-password")
    suspend fun resetPassword(
        @Query("email") email: String,
        @Query("newPassword") newPassword: String
    ): Response<ResponseBody>

    @POST("v1/change-password")
    suspend fun changePassword(
        @Query("email") email: String,
        @Body request: ChangePasswordRequest
    ): Response<ResponseBody>
}