package com.pht.vntechpc.data.remote.model.response

data class AuthResponse(
    val expiresIn: Int,
    val role: String,
    val message: String,
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String
)