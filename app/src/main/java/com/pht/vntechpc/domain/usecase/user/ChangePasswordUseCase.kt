package com.pht.vntechpc.domain.usecase.user

import com.pht.vntechpc.domain.repository.UserRepository
import jakarta.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) = userRepository.changePassword(email, oldPassword, newPassword, confirmNewPassword)
}