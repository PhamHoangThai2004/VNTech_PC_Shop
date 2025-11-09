package com.pht.vntechpc.domain.model

data class User (
    val email: String,
    val username: String,
    val fullName: String,
    val gender: String?,
    val avatar: String?,
    val dateOfBirth: String?
)