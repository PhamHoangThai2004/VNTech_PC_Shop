package com.pht.vntechpc.domain.usecase.order

import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(
        addressId: Int,
        paymentMethod: String,
        note: String,
        couponCode: String
    ) = orderRepository.createOrder(addressId, paymentMethod, note, couponCode)
}