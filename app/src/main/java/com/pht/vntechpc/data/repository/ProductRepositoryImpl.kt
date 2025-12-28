package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.response.PagedProductsResponse
import com.pht.vntechpc.data.remote.model.response.toProduct
import com.pht.vntechpc.data.remote.service.ProductService
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.domain.repository.BaseRepository
import com.pht.vntechpc.domain.repository.ProductRepository
import jakarta.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val service: ProductService) : BaseRepository(),
    ProductRepository {
    override suspend fun fetchProducts(
        categoryId: Int?,
        productName: String?,
        brand: String?,
        minPrice: Long?,
        maxPrice: Long?,
        page: Int,
        size: Int
    ): Result<PagedProductsResponse> {
        return apiCallRaw { service.fetchProducts(
            categoryId,
            productName,
            brand,
            minPrice,
            maxPrice,
            page,
            size
        ) }
    }

    override suspend fun fetchProductById(productId: Int): Result<Product> {
        return apiCall({ service.fetchProductById(productId) }, { it.toProduct() })
    }
}