package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.request.CartRequest
import com.pht.vntechpc.data.remote.model.request.UpdateCartItemRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.CartItemResponse
import com.pht.vntechpc.data.remote.model.response.CartResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CartApi {
    @GET("v1/cart")
    suspend fun fetchCart(): Response<BaseResponse<CartResponse>>

    @GET("v1/cart/count")
    suspend fun fetchCartCount(): Response<BaseResponse<Int>>

    @POST("v1/cart/items")
    suspend fun addToCart(
        @Body request: CartRequest
    ): Response<BaseResponse<CartResponse>>

    @PUT("v1/cart/items/{cartItemId}")
    suspend fun updateQuantity(
        @Path("cartItemId") cartItemId: Int,
        @Body request: UpdateCartItemRequest
    ): Response<BaseResponse<CartResponse>>

    @DELETE("v1/cart/items/{cartItemId}")
    suspend fun removeFromCart(
        @Path("cartItemId") cartItemId: Int
    ): Response<BaseResponse<Unit>>

    @DELETE("v1/cart")
    suspend fun clearCart(): Response<BaseResponse<Unit>>

    @GET("v1/cart/selected-items")
    suspend fun fetchSelectedCartItems(): Response<BaseResponse<List<CartItemResponse>>>

    @PUT("v1/cart/items/select")
    suspend fun selectCartItem(
        @Query("itemIds") cartItemId: Int,
        @Query("selected") selected: Boolean
    ): Response<BaseResponse<CartResponse>>
}