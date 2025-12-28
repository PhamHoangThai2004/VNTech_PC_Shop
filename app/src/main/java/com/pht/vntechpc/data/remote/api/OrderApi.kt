package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.request.CalculateShippingRequest
import com.pht.vntechpc.data.remote.model.request.CreateOrderRequest
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.OrderResponse
import com.pht.vntechpc.data.remote.model.response.OrderShortResponse
import com.pht.vntechpc.data.remote.model.response.PaymentMethodResponse
import com.pht.vntechpc.data.remote.model.response.ShippingResponse
import com.pht.vntechpc.data.remote.network.NoAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {
    @POST("v1/user/orders")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): Response<BaseResponse<Unit>>

    @POST("v1/user/orders/{orderId}/cancel")
    suspend fun cancelOrder(
        @Path("orderId") orderId: Int,
        @Query("reason") reason: String
    ): Response<BaseResponse<Unit>>

    @GET("v1/user/orders/{orderId}")
    suspend fun fetchOrderById(
        @Path("orderId") orderId: Int
    ): Response<BaseResponse<OrderResponse>>

    @GET("v1/user/orders/my")
    suspend fun fetchMyOrders(): Response<BaseResponse<List<OrderShortResponse>>>

    @GET("v1/user/orders/my/status")
    suspend fun fetchMyOrdersByStatus(
        @Query("status") status: String
    ): Response<BaseResponse<List<OrderShortResponse>>>

    @GET("v1/user/orders/code/{orderCode}")
    suspend fun fetchOrderByCode(
        @Path("orderCode") orderCode: String
    ): Response<BaseResponse<OrderResponse>>

    @NoAuth
    @GET("v1/payment-methods")
    suspend fun fetchPaymentMethods(): Response<BaseResponse<List<PaymentMethodResponse>>>

    @NoAuth
    @POST("v1/shipping/calculate")
    suspend fun calculateShipping(
        @Body request: CalculateShippingRequest
    ): Response<BaseResponse<ShippingResponse>>
}