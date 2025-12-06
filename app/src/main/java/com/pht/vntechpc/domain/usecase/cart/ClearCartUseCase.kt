package com.pht.vntechpc.domain.usecase.cart

import com.pht.vntechpc.domain.repository.CartRepository
import jakarta.inject.Inject

class ClearCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke() = cartRepository.clearCart()
}