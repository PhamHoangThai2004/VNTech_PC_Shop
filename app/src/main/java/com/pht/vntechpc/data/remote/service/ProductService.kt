package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.ProductApi
import javax.inject.Inject

class ProductService @Inject constructor(private val api: ProductApi) {
    suspend fun fetchProducts(
        categoryId: Int?,
        productName: String?,
        brand: String?,
        minPrice: Long?,
        maxPrice: Long?,
        page: Int,
        size: Int
    ) = api.fetchProducts(
        categoryId,
        productName,
        brand,
        minPrice,
        maxPrice,
        page,
        size
    )

    suspend fun fetchProductById(productId: Int) = api.fetchProductById(productId)
}