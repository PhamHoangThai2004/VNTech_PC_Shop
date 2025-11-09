package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.response.PagedProductsResponse
import com.pht.vntechpc.domain.model.Product

interface ProductRepository {
    suspend fun fetchProducts(): Result<PagedProductsResponse>

    suspend fun fetchProductById(productId: Int): Result<Product>
}