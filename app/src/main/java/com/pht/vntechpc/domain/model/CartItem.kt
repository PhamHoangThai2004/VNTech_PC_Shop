package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Int,
    val quantity: Int,
    val price: Long,
    val selected: Boolean,
    val product: ProductInCart
) : Parcelable