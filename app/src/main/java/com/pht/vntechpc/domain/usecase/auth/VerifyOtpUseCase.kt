package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.domain.repository.AuthRepository
import jakarta.inject.Inject

class VerifyOtpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(otp: String) = authRepository.verifyOtp(otp)
}