package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Cart
import com.pht.vntechpc.domain.model.CartItem
import com.pht.vntechpc.domain.model.ProductInCart

data class CartResponse(
    val cartId: Int,
    val userId: Int,
    val cartItems: List<CartItemResponse>,
    val selectedItems: Int,
    val totalItems: Int,
    val selectedItemsPrice: Long,
    val totalPrice: Long
)

fun CartResponse.toCart(): Cart {
    return Cart(
        cartId,
        userId,
        totalItems,
        totalPrice,
        selectedItems,
        selectedItemsPrice,
        cartItems.map { it.toCartItem() },
    )
}

data class CartItemResponse(
    val id: Int,
    val quantity: Int,
    val price: Long,
    val selected: Boolean,
    val product: ProductInCartResponse,
)

fun CartItemResponse.toCartItem(): CartItem {
    return CartItem(
        id,
        quantity,
        price,
        selected,
        product.toProductInCart()
    )
}

data class ProductInCartResponse(
    val id: Int,
    val productName: String,
    val salePrice: Long,
    val brand: String,
    val model: String,
    val stock: Int,
    val mainImage: String?,
)

fun ProductInCartResponse.toProductInCart(): ProductInCart {
    return ProductInCart(
        id,
        productName,
        salePrice,
        brand,
        model,
        stock,
        mainImage
    )
}