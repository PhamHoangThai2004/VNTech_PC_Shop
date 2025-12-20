package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Order
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.domain.usecase.product.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderDetailUIState())
    val uiState: MutableStateFlow<OrderDetailUIState> = _uiState

    fun setOrder(order: Order) {
        _uiState.value = _uiState.value.copy(order = order)
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = OrderDetailState.None)
    }

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getProductDetailUseCase(productId)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = OrderDetailState.Success(it))
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = OrderDetailState.Failure(it.message.toString()))
            }
        }
    }

}

data class OrderDetailUIState(
    val order: Order? = null,
    val status: OrderDetailState = OrderDetailState.None
)

sealed class OrderDetailState {
    object None : OrderDetailState()
    data class Loading(val message: String) : OrderDetailState()
    data class Success(val product: Product) : OrderDetailState()
    data class Failure(val message: String) : OrderDetailState()
}