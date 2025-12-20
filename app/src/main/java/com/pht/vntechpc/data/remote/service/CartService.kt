package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.CartApi
import com.pht.vntechpc.data.remote.model.request.CartRequest
import com.pht.vntechpc.data.remote.model.request.UpdateCartItemRequest
import jakarta.inject.Inject

class CartService @Inject constructor(private val api: CartApi) {
    suspend fun fetchCartByUserId() = api.fetchCart()

    suspend fun fetchCartCountByUserId() = api.fetchCartCount()

    suspend fun addProductToCart(request: CartRequest) = api.addToCart(request)

    suspend fun updateProductQuantity(cartItemId: Int, request: UpdateCartItemRequest) =
        api.updateQuantity(cartItemId, request)

    suspend fun removeProductFromCart(cartItemId: Int) =
        api.removeFromCart(cartItemId)

    suspend fun clearCart() = api.clearCart()

    suspend fun selectCartItem(cartItemId: Int, selected: Boolean) =
        api.selectCartItem(cartItemId, selected)
}