package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    val id: Int,
    val orderCode: String,
    val status: String,
    val totalPrice: Long,
    val shippingFee: Long,
    val discount: Long,
    val finalPrice: Long,
    val note: String,
    val createdAt: String,
    val updatedAt: String?,
    val confirmedAt: String?,
    val processingAt: String?,
    val shippingAt: String?,
    val deliveredAt: String?,
    val cancelledAt: String?,
    val cancelReason: String?,
    val user: User,
    val address: Address,
    val payment: String?,
    val paymentMethod: String? ,
    val orderItems: List<OrderItem>,
    val canBeCancelled: Boolean,
) : Parcelable