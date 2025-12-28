package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.request.CalculateShippingRequest
import com.pht.vntechpc.data.remote.model.request.CreateOrderRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.toOrder
import com.pht.vntechpc.data.remote.model.response.toOrderShort
import com.pht.vntechpc.data.remote.model.response.toPaymentMethod
import com.pht.vntechpc.data.remote.model.response.toShipping
import com.pht.vntechpc.data.remote.service.OrderService
import com.pht.vntechpc.domain.model.Order
import com.pht.vntechpc.domain.model.OrderShort
import com.pht.vntechpc.domain.model.PaymentMethod
import com.pht.vntechpc.domain.model.Shipping
import com.pht.vntechpc.domain.repository.BaseRepository
import com.pht.vntechpc.domain.repository.OrderRepository
import jakarta.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val service: OrderService
) : BaseRepository(), OrderRepository {
    override suspend fun createOrder(
        addressId: Int,
        paymentMethod: String,
        note: String,
        couponCode: String
    ): Result<BaseResponse<Unit>> {
        val request = CreateOrderRequest(addressId, paymentMethod, note, couponCode)
        return apiCallNoData { service.createOrder(request) }
    }

    override suspend fun cancelOrder(
        orderId: Int,
        reason: String
    ): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.cancelOrder(orderId, reason) }
    }

    override suspend fun fetchOrderById(orderId: Int): Result<Order> {
        return apiCall({ service.fetchOrderById(orderId) }) { it.toOrder() }
    }

    override suspend fun fetchMyOrders(): Result<List<OrderShort>> {
        return apiCall({ service.fetchMyOrders() }) { it -> it.map { it.toOrderShort() } }
    }

    override suspend fun fetchMyOrdersByStatus(status: String): Result<List<OrderShort>> {
        return apiCall({ service.fetchMyOrdersByStatus(status) }) { it -> it.map { it.toOrderShort() } }
    }

    override suspend fun fetchOrderByCode(orderCode: String): Result<Order> {
        return apiCall({ service.fetchOrderByCode(orderCode) }) { it.toOrder() }
    }

    override suspend fun fetchPaymentMethods(): Result<List<PaymentMethod>> {
        return apiCall({ service.fetchPaymentMethods() }) { it -> it.map { it.toPaymentMethod() } }
    }

    override suspend fun calculateShipping(
        province: String,
        orderValue: Long
    ): Result<Shipping> {
        val request = CalculateShippingRequest(province, orderValue)
        return apiCall({ service.calculateShipping(request) }) { it.toShipping() }
    }
}