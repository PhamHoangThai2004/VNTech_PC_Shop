package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.domain.model.Order
import com.pht.vntechpc.domain.model.OrderShort

interface OrderRepository {
    suspend fun createOrder(
        addressId: Int,
        paymentMethod: String,
        note: String,
        couponCode: String
    ): Result<BaseResponse<Unit>>

    suspend fun cancelOrder(orderId: Int, reason: String): Result<BaseResponse<Unit>>

    suspend fun fetchOrderById(orderId: Int): Result<Order>

    suspend fun fetchMyOrders(): Result<List<OrderShort>>

    suspend fun fetchMyOrdersByStatus(status: String): Result<List<OrderShort>>

    suspend fun fetchOrderByCode(orderCode: String): Result<Order>
}