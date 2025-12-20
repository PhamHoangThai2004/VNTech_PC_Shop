package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Order
import com.pht.vntechpc.domain.model.OrderShort
import com.pht.vntechpc.domain.usecase.order.GetMyOrdersByStatusUseCase
import com.pht.vntechpc.domain.usecase.order.GetMyOrdersUseCase
import com.pht.vntechpc.domain.usecase.order.GetOrderByCodeUseCase
import com.pht.vntechpc.domain.usecase.order.GetOrderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val getMyOrdersUseCase: GetMyOrdersUseCase,
    private val getMyOrdersByStatusUseCase: GetMyOrdersByStatusUseCase,
    private val getOrderByCodeUseCase: GetOrderByCodeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUIState())
    val uiState: StateFlow<OrderUIState> = _uiState


    fun resetState() {
        _uiState.value = _uiState.value.copy(status = OrderState.None)
    }

    fun getOrderById(orderId: Int) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { getOrderDetailUseCase(orderId) }
            result.onSuccess {
                Log.i("BBB", "Success: $it")
                _uiState.value = _uiState.value.copy(status = OrderState.Success(it))
            }.onFailure {
                Log.e("BBB", "Error: ${it.message}")
                _uiState.value = _uiState.value.copy(status = OrderState.Failure(it.message.toString()))
            }
        }
    }

    fun getMyOrder() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = OrderState.Loading("Đang tải..."))
            val result = withContext(Dispatchers.IO) {
                getMyOrdersUseCase()
            }
            result.onSuccess {
                Log.i("BBB", "Success: $it")
                _uiState.value = _uiState.value.copy(orders = it, status = OrderState.None)
            }.onFailure {
                Log.e("BBB", "Error: ${it.message}")
                _uiState.value = _uiState.value.copy(status = OrderState.Failure(it.message.toString()))
            }
        }
    }

    fun getMyOrdersByStatus(status: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getMyOrdersByStatusUseCase(status)
            }
            result.onSuccess {
                Log.i("BBB", "Success: $it")
            }.onFailure {
                Log.e("BBB", "Error: ${it.message}")
            }
        }
    }

    fun getOrderByCode(orderCode: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getOrderByCodeUseCase(orderCode)
            }
            result.onSuccess {
                Log.i("BBB", "Success: $it")
            }.onFailure {
                Log.e("BBB", "Error: ${it.message}")
            }
        }
    }
}

data class OrderUIState(
    val orders: List<OrderShort> = emptyList(),
    val status: OrderState = OrderState.None
)

sealed class OrderState {
    object None : OrderState()
    data class Loading(val message: String) : OrderState()
    data class Success(val order: Order) : OrderState()
    data class Failure(val message: String) : OrderState()
}