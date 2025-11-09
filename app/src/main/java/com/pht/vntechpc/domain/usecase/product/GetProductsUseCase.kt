package com.pht.vntechpc.domain.usecase.product

import com.pht.vntechpc.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke() = productRepository.fetchProducts()
}