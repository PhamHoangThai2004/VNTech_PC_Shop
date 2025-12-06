package com.pht.vntechpc.domain.usecase.cart

import com.pht.vntechpc.domain.repository.CartRepository
import jakarta.inject.Inject

class RemoveCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: Int) = cartRepository.removeProductFromCart(cartItemId)
}