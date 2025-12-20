package com.pht.vntechpc.domain.usecase.order

import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class GetMyOrdersByStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(status: String) = orderRepository.fetchMyOrdersByStatus(status)
}