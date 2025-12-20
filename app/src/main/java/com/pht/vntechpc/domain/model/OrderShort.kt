package com.pht.vntechpc.domain.model

data class OrderShort (
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