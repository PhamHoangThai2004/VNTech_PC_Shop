package com.pht.vntechpc.domain.usecase.order

import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class GetMyOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke() = orderRepository.fetchMyOrders()
}