package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.CategoryResponse
import com.pht.vntechpc.data.remote.network.NoAuth
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApi {
    @NoAuth
    @GET("v1/categories")
    suspend fun fetchCategories(): Response<BaseResponse<List<CategoryResponse>>>

    @NoAuth
    @GET("v1/categories/{id}")
    suspend fun fetchCategoryById(@Path("id") categoryId: Int): Response<BaseResponse<CategoryResponse>>
}