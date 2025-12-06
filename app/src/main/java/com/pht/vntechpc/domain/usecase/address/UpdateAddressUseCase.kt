package com.pht.vntechpc.domain.usecase.address

import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.repository.AddressRepository
import jakarta.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(addressId: Int, address: Address) =
        repository.updateAddress(addressId, address)
}