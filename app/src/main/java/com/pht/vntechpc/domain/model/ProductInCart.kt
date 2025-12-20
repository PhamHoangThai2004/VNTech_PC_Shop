package com.pht.vntechpc.domain.model

data class ProductInCart(
    val id: Int,
    val productName: String,
    val salePrice: Long,
    val brand: String?,
    val model: String,
    val stock: Int,
    val mainImage: String?
)