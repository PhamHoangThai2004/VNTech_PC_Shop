package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: Int,
    val productName: String,
    val description: String,
    val originalPrice: Long,
    val salePrice: Long?,
    val stock: Int,
    val quantitySold: Int,
    val brand: String?,
    val model: String,
    val rating: Float,
    val origin: String?,
    val createdAt: String,
    val updatedAt: String,
    val category: Category,
    val images: List<Image>?,
    val specifications: List<Specification>?
) : Parcelable