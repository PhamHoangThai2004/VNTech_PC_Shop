package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Order
import com.pht.vntechpc.domain.model.OrderItem

data class OrderResponse (
    val id: Int,
    val orderCode: String,
    val status: String,
    val statusName: String,
    val statusDescription: String,
    val totalPrice: Long,
    val shippingFee: Long,
    val discount: Long,
    val finalPrice: Long,
    val note: String,
    val createdAt: String,
    val updatedAt: String,
    val confirmedAt: String,
    val processingAt: String,
    val shippingAt: String,
    val deliveredAt: String,
    val cancelledAt: String,
    val cancelReason: String,
    val user: UserResponse,
    val address: AddressResponse,
    val payment: String,
    val paymentMethod: String,
    val orderItems: List<OrderItemResponse>,
    val canBeCancelled: Boolean,
    val nextPossibleStatuses: List<String>
)

fun OrderResponse.toOrder(): Order {
    return Order(
        id,
        orderCode,
        status,
        totalPrice,
        shippingFee,
        discount,
        finalPrice,
        note,
        createdAt,
        updatedAt,
        confirmedAt,
        processingAt,
        shippingAt,
        deliveredAt,
        cancelledAt,
        cancelReason,
        user.toUser(),
        address.toAddress(),
        payment,
        paymentMethod,
        orderItems.map { it.toOrderItem() },
        canBeCancelled
    )
}

data class OrderItemResponse (
    val id: Int,
    val price: Long,
    val quantity: Int,
    val totalPrice: Long,
    val product: OrderedProductResponse
)

fun OrderItemResponse.toOrderItem(): OrderItem {
    return OrderItem(
        id,
        price,
        quantity,
        totalPrice,
        product.id,
        product.productName,
        product.brand,
        product.model,
        product.mainImage)
}

data class OrderedProductResponse (
    val id: Int,
    val productName: String,
    val salePrice: Long,
    val brand: String,
    val model: String,
    val stock: Int,
    val mainImage: String
)