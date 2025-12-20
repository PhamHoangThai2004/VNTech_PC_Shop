package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.OrderApi
import com.pht.vntechpc.data.remote.model.request.CreateOrderRequest
import jakarta.inject.Inject

class OrderService @Inject constructor(private val api: OrderApi) {
    suspend fun createOrder(request: CreateOrderRequest) = api.createOrder(request)

    suspend fun cancelOrder(orderId: Int, reason: String) = api.cancelOrder(orderId, reason)

    suspend fun fetchOrderById(orderId: Int) = api.fetchOrderById(orderId)

    suspend fun fetchMyOrders() = api.fetchMyOrders()

    suspend fun fetchMyOrdersByStatus(status: String) = api.fetchMyOrdersByStatus(status)

    suspend fun fetchOrderByCode(orderCode: String) = api.fetchOrderByCode(orderCode)
}