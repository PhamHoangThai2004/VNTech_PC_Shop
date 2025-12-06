package com.pht.vntechpc.domain.usecase.user

import com.pht.vntechpc.domain.repository.UserRepository
import jakarta.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        fullName: String,
        gender: String,
        dateOfBirth: String
    ) = userRepository.updateUser(email, fullName, gender, dateOfBirth)
}