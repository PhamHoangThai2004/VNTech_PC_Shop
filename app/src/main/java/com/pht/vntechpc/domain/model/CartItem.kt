package com.pht.vntechpc.domain.model

data class CartItem(
    val id: Int,
    val quantity: Int,
    val price: Long,
    val product: ProductInCart
)