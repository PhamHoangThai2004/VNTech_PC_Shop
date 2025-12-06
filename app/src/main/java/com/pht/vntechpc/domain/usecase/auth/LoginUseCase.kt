package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.login(email, password)
}