package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.data.remote.service.AuthService
import com.pht.vntechpc.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyResetOtpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(otp: String) = authRepository.verifyResetOtp(otp)
}