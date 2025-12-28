package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.PaymentMethod

data class PaymentMethodResponse (
    val code: String,
    val name: String,
    val note: String
)

fun PaymentMethodResponse.toPaymentMethod() = PaymentMethod(
    code,
    name
)