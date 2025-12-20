package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Cart
import com.pht.vntechpc.domain.usecase.cart.ClearCartUseCase
import com.pht.vntechpc.domain.usecase.cart.GetCartUseCase
import com.pht.vntechpc.domain.usecase.cart.RemoveCartItemUseCase
import com.pht.vntechpc.domain.usecase.cart.SelectCartItemUseCase
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
    private val clearCartUseCase: ClearCartUseCase,
    private val selectCartItemUseCase: SelectCartItemUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = CartState.None)
    }

    fun getCart() {
        _uiState.value = _uiState.value.copy(status = CartState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { getCartUseCase() }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    cart = it,
                    status = CartState.None
                )
                Log.d("BBB", "Cart: $it")
            }.onFailure {
                Log.d("BBB", "Get Cart Faults: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        if (newQuantity !in 1..99) {
            Log.d("BBB", "Invalid quantity: $newQuantity")
            return
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                updateQuantityUseCase(itemId, newQuantity)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(cart = it)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun removeItem(itemId: Int) {
        _uiState.value = _uiState.value.copy(status = CartState.Loading("Đang xoá..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                removeCartItemUseCase(itemId)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = CartState.None)
                getCart()
            }.onFailure {
                Log.d("BBB", "Remove Item Faults: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun confirmClearCart() {
        _uiState.value = _uiState.value.copy(
            status = CartState.ConfirmClear("Bạn có chắc chắn muốn xoá tất cả sản phẩm trong giỏ hàng?")
        )
    }

    fun clearCart() {
        _uiState.value = _uiState.value.copy(status = CartState.Loading("Đang xoá..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                clearCartUseCase()
            }
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        cart = null,
                        status = CartState.None
                    )
                }
            }.onFailure {
                _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

    fun toggleSelectedItem(itemId: Int, selected: Boolean) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                selectCartItemUseCase(
                    itemId, selected
                )
            }
            result.onSuccess {
                Log.d("BBB", "Toggle Selected Item Success: $it")
                _uiState.value = _uiState.value.copy(cart = it)
            }.onFailure {
                _uiState.value.copy(status = CartState.Failure(it.message.toString()))
            }
        }
    }

}

data class CartUiState(
    val cart: Cart? = null,
    val status: CartState = CartState.None
)

sealed class CartState {
    object None : CartState()
    data class ConfirmClear(val message: String) : CartState()
    data class Loading(val message: String) : CartState()
    data class Failure(val message: String) : CartState()
}