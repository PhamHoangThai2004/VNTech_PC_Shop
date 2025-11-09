package com.pht.vntechpc.domain.usecase.product

import com.pht.vntechpc.domain.repository.ProductRepository
import jakarta.inject.Inject

class GetProductDetailUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: Int) = productRepository.fetchProductById(productId)
}