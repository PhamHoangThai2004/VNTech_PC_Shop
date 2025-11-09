package com.pht.vntechpc.domain.usecase.user

import com.pht.vntechpc.domain.repository.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.fetchUser()
}