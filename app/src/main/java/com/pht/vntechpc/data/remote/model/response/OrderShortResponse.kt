package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.OrderShort

data class OrderShortResponse(
    val orderId: Int,
    val orderCode: String,
    val status: String,
    val finalPrice: Long,
    val createdAt: String,
    val recipientName: String,
    val phoneNumber: String,
    val shortAddress: String,
    val itemCount: Int
)

fun OrderShortResponse.toOrderShort(): OrderShort {
    return OrderShort(
        orderId,
        orderCode,
        status,
        finalPrice,
        createdAt,
        recipientName,
        phoneNumber,
        shortAddress,
        itemCount
    )
}