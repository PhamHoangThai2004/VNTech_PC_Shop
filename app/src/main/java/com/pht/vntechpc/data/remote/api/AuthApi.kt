package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.request.LoginRequest
import com.pht.vntechpc.data.remote.model.request.RefreshTokenRequest
import com.pht.vntechpc.data.remote.model.request.RegisterRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.AuthResponse
import com.pht.vntechpc.data.remote.network.NoAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @NoAuth
    @POST("v1/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<AuthResponse>>

    @NoAuth
    @POST("v1/register")
    suspend fun register(@Body request: RegisterRequest): Response<BaseResponse<Unit>>

    @NoAuth
    @POST("v1/verify-otp")
    suspend fun verifyOtp(@Query("otp") otp: String): Response<BaseResponse<Unit>>

    @NoAuth
    @POST("v1/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): Response<BaseResponse<Unit>>

    @NoAuth
    @POST("v1/verify-reset-otp")
    suspend fun verifyResetOtp(@Query("otp") otp: String): Response<BaseResponse<Unit>>

    @NoAuth
    @POST("v1/reset-password")
    suspend fun resetPassword(
        @Query("email") email: String,
        @Query("newPassword") newPassword: String
    ): Response<BaseResponse<Unit>>

    @NoAuth
    @POST("v1/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<BaseResponse<AuthResponse>>
}