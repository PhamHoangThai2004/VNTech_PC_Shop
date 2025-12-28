package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.PagedProductsResponse
import com.pht.vntechpc.data.remote.model.response.ProductResponse
import com.pht.vntechpc.data.remote.network.NoAuth
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @NoAuth
    @GET("v1/products")
    suspend fun fetchProducts(
        @Query("categoryId") categoryId: Int?,
        @Query("productName") productName: String?,
        @Query("brand") brand: String?,
        @Query("minPrice") minPrice: Long?,
        @Query("maxPrice") maxPrice: Long?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<BaseResponse<PagedProductsResponse>>

    @NoAuth
    @GET("v1/products/{id}")
    suspend fun fetchProductById(@Path("id") productId: Int): Response<BaseResponse<ProductResponse>>
}