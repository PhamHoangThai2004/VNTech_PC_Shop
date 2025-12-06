package com.pht.vntechpc.domain.model

data class Cart(
    val cartId: Int,
    val userId: Int,
    val totalItems: Int,
    val totalPrice: Long,
    val cartItems: List<CartItem>
)