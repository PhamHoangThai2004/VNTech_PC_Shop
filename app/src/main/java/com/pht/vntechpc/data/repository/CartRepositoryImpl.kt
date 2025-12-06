package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.request.CartRequest
import com.pht.vntechpc.data.remote.model.request.UpdateCartItemRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.toCart
import com.pht.vntechpc.data.remote.service.CartService
import com.pht.vntechpc.domain.model.Cart
import com.pht.vntechpc.domain.repository.BaseRepository
import com.pht.vntechpc.domain.repository.CartRepository
import jakarta.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService
) : CartRepository, BaseRepository() {
    override suspend fun fetchCartByUserId(): Result<Cart> {
        return apiCall({ cartService.fetchCartByUserId() }, { it.toCart() })
    }

    override suspend fun fetchCartCountByUserId(): Result<Int> {
        return apiCall({ cartService.fetchCartCountByUserId() }, { it })
    }

    override suspend fun addProductToCart(
        productId: Int,
        quantity: Int
    ): Result<Cart> {
        val request = CartRequest(productId, quantity)
        return apiCall({ cartService.addProductToCart(request) }, { it.toCart() })
    }

    override suspend fun updateProductQuantity(
        cartItemId: Int,
        quantity: Int
    ): Result<Cart> {
        val request = UpdateCartItemRequest(quantity)
        return apiCall(
            { cartService.updateProductQuantity(cartItemId, request) },
            { it.toCart() })
    }

    override suspend fun removeProductFromCart(
        cartItemId: Int
    ): Result<BaseResponse<Unit>> {
        return apiCallNoData { cartService.removeProductFromCart(cartItemId) }
    }

    override suspend fun clearCart(): Result<BaseResponse<Unit>> {
        return apiCallNoData { cartService.clearCart() }
    }
}