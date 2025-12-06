package com.pht.vntechpc.domain.usecase.address

import com.pht.vntechpc.domain.repository.AddressRepository
import jakarta.inject.Inject

class GetWardsUseCase @Inject constructor(private val repository: AddressRepository) {
    suspend operator fun invoke(districtCode: Int) = repository.fetchWards(districtCode)
}