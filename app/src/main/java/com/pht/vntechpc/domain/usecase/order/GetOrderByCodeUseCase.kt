package com.pht.vntechpc.domain.usecase.order

import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class GetOrderByCodeUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderCode: String) = orderRepository.fetchOrderByCode(orderCode)
}