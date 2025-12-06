package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Cart
import com.pht.vntechpc.domain.usecase.cart.ClearCartUseCase
import com.pht.vntechpc.domain.usecase.cart.GetCartUseCase
import com.pht.vntechpc.domain.usecase.cart.RemoveCartItemUseCase
import com.pht.vntechpc.domain.usecase.cart.UpdateQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.update

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateQuantityUseCase: UpdateQuantityUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = CartState.None)
    }

    fun getCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = CartState.Loading("Đang tải..."))
            val result = withContext(Dispatchers.IO) { getCartUseCase() }
            result.onSuccess {
                val cart = it
                _uiState.value = _uiState.value.copy(
                    itemCount = cart.cartItems.size,
                    cart = cart,
                    status = CartState.None
                )
                Log.d("BBB", "Cart: $cart")
            }.onFailure {
                Log.d("BBB", "Get Cart Faults: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        if (newQuantity <= 0 || newQuantity > 99) {
            Log.d("BBB", "Invalid quantity: $newQuantity")
            return
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                updateQuantityUseCase(itemId, newQuantity)
            }
            result.onSuccess {
                Log.d("BBB", "Update Quantity Success: $it")
                _uiState.value = _uiState.value.copy(cart = it)
            }.onFailure {
                Log.d("BBB", "Update Quantity Faults: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                removeCartItemUseCase(itemId)
            }
            result.onSuccess {
                Log.d("BBB", "Remove Item Success: $it")
                getCart()
            }.onFailure {
                Log.d("BBB", "Remove Item Faults: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                clearCartUseCase()
            }
            result.onSuccess { it ->
                Log.d("BBB", "Clear Cart Success: $it")
                _uiState.update {
                    it.copy(
                        itemCount = 0,
                        cart = null,
                    )
                }
            }.onFailure {
                Log.d("BBB", "Clear Cart Faults: ${it.message}")
                _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }

    }

}

data class CartUiState(
    val itemCount: Int = 0,
    val cart: Cart? = null,
    val selectedItemIds: List<Int> = emptyList(),
    val status: CartState = CartState.None
)

sealed class CartState {
    object None : CartState()
    data class Loading(val message: String) : CartState()
    data class Failure(val message: String) : CartState()
}