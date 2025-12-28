package com.pht.vntechpc.data.remote.model.request

data class CalculateShippingRequest (
    val province: String,
    val orderValue: Long
)