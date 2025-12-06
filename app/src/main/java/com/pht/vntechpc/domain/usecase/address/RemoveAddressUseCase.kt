package com.pht.vntechpc.domain.usecase.address

import com.pht.vntechpc.domain.repository.AddressRepository
import jakarta.inject.Inject

class RemoveAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(addressId: Int) = repository.removeAddress(addressId)
}