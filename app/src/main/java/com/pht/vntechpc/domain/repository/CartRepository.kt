package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.request.CartRequest
import com.pht.vntechpc.data.remote.model.request.UpdateCartItemRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.domain.model.Cart
import com.pht.vntechpc.domain.model.CartItem

interface CartRepository {
    suspend fun fetchCartByUserId(): Result<Cart>

    suspend fun fetchCartCountByUserId(): Result<Int>

    suspend fun addProductToCart(
        productId: Int,
        quantity: Int
    ): Result<Cart>

    suspend fun updateProductQuantity(
        cartItemId: Int,
        quantity: Int
    ): Result<Cart>

    suspend fun removeProductFromCart( cartItemId: Int): Result<BaseResponse<Unit>>

    suspend fun clearCart(): Result<BaseResponse<Unit>>

    suspend fun fetchSelectedCartItems(): Result<List<CartItem>>

    suspend fun selectCartItem(cartItemId: Int, selected: Boolean): Result<Cart>
}