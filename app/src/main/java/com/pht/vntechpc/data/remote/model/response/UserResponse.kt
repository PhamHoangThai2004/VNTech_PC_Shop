package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.User

data class UserResponse (
    val id: Int,
    val email: String,
    val username: String,
    val fullName: String,
    val gender: String,
    val avatar: String,
    val dateOfBirth: String
)

fun UserResponse.toUser(): User {
    return User(id, email, fullName, gender, avatar, dateOfBirth)
}