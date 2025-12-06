package com.pht.vntechpc.domain.usecase.address

import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.repository.AddressRepository
import jakarta.inject.Inject

class AddNewAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(address: Address) = addressRepository.addAddress(address)
}