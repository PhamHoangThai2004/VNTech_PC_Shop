package com.pht.vntechpc.domain.usecase.cart

import com.pht.vntechpc.domain.model.CartItem
import com.pht.vntechpc.domain.repository.CartRepository
import jakarta.inject.Inject

class GetSelectedCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Result<List<CartItem>> {
        return cartRepository.fetchSelectedCartItems()
    }
}