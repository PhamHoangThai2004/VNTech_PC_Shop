package com.pht.vntechpc.domain.usecase.order

import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class GetOrderDetailUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int) = orderRepository.fetchOrderById(orderId)
}