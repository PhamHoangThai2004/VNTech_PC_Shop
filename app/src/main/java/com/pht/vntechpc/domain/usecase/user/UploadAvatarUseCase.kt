package com.pht.vntechpc.domain.usecase.user

import com.pht.vntechpc.domain.repository.UserRepository
import jakarta.inject.Inject
import java.io.File

class UploadAvatarUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: Int, file: File) = repository.uploadAvatar(userId, file)

}