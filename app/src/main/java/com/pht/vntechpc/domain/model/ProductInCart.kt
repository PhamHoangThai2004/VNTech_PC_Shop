package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInCart(
    val id: Int,
    val productName: String,
    val salePrice: Long,
    val brand: String?,
    val model: String,
    val stock: Int,
    val mainImage: String?
) : Parcelable