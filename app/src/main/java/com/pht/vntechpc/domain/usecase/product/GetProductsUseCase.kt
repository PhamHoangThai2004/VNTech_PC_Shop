package com.pht.vntechpc.domain.usecase.product

import com.pht.vntechpc.domain.repository.ProductRepository
import jakarta.inject.Inject

class GetProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(
        categoryId: Int? = null,
        productName: String? = null,
        brand: String? = null,
        minPrice: Long? = null,
        maxPrice: Long? = null,
        page: Int = 0,
        size: Int = 10,
    ) = productRepository.fetchProducts(
        categoryId,
        productName,
        brand,
        minPrice,
        maxPrice,
        page,
        size
    )
}