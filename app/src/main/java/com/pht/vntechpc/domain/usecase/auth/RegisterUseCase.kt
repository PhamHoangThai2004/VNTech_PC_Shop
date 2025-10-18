package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.domain.repository.AuthRepository
import jakarta.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, name: String) =
        authRepository.register(email, password, name)
}