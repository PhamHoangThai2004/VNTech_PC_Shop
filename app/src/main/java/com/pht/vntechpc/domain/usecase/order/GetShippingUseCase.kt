package com.pht.vntechpc.domain.usecase.order

import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class GetShippingUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(province: String, orderValue: Long) =
        repository.calculateShipping(province, orderValue)
}