package com.pht.vntechpc.domain.model

data class Shipping (
    val shippingFee: Long,
    val baseFee : Long,
    val isFreeShipping: Boolean,
    val estimatedDays: Int
)