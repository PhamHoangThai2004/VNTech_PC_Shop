package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.ProductApi
import javax.inject.Inject

class ProductService @Inject constructor(private val api: ProductApi) {
    suspend fun fetchProducts() = api.fetchProducts()

    suspend fun fetchProductById(productId: Int) = api.fetchProductById(productId)
}