package com.pht.vntechpc.data.remote.model.request

class CreateOrderRequest(
    val addressId: Int,
    val paymentMethod: String,
    val note: String,
    val couponCode: String
)