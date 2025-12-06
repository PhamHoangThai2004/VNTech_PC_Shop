package com.pht.vntechpc.domain.usecase.auth

import com.pht.vntechpc.domain.repository.AuthRepository
import jakarta.inject.Inject

class RefreshTokenUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(refreshToken: String) = repository.refreshToken(refreshToken)
}