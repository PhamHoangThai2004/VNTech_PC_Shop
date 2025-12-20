package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem (
    val id: Int,
    val price: Long,
    val quantity: Int,
    val totalPrice: Long,
    val productId: Int,
    val productName: String,
    val brand: String?,
    val model: String,
    val mainImage: String
) : Parcelable