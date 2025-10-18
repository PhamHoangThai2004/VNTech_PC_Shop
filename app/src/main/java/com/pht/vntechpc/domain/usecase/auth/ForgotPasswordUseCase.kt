package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.data.remote.service.AuthService
import com.pht.vntechpc.domain.repository.AuthRepository
import jakarta.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String) = authRepository.forgotPassword(email)
}