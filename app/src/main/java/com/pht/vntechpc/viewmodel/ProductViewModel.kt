package com.pht.vntechpc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.domain.usecase.cart.AddProductToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val addProductToCartUseCase: AddProductToCartUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: MutableStateFlow<ProductUiState> = _uiState

    fun setProduct(product: Product) {
        _uiState.value = _uiState.value.copy(product = product)
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = ProductState.None)
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                addProductToCartUseCase(productId, 1)
            }
            result.onSuccess {
                _uiState.value =
                    _uiState.value.copy(status = ProductState.Success("Thêm thành công sản phẩm vào giỏ hàng"))
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = ProductState.Failure(it.message.toString()))
            }

        }
    }
}

data class ProductUiState(
    val product: Product? = null,
    val status: ProductState = ProductState.None
)

sealed class ProductState {
    object None : ProductState()
    data class Loading(val message: String) : ProductState()
    data class Success(val message: String) : ProductState()
    data class Failure(val message: String) : ProductState()
}