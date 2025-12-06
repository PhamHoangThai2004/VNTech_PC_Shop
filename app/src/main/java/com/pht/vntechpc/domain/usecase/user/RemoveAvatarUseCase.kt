package com.pht.vntechpc.domain.usecase.user

import com.pht.vntechpc.domain.repository.UserRepository
import jakarta.inject.Inject

class RemoveAvatarUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: Int) = repository.deleteAvatar(userId)
}