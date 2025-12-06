package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.domain.repository.AuthRepository
import jakarta.inject.Inject

class VerifyResetOtpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(otp: String) = authRepository.verifyResetOtp(otp)
}