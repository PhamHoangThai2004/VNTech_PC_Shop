package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Shipping

data class ShippingResponse (
    val shippingFee: Long,
    val province: String,
    val distanceKm: Int,
    val baseFee : Long,
    val isFreeShipping: Boolean,
    val estimatedDays: Int
)

fun ShippingResponse.toShipping() = Shipping(
    shippingFee,
    baseFee,
    isFreeShipping,
    estimatedDays
)