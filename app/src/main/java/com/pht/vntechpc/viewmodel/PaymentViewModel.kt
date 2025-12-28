package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.model.CartItem
import com.pht.vntechpc.domain.model.PaymentMethod
import com.pht.vntechpc.domain.model.Shipping
import com.pht.vntechpc.domain.usecase.address.GetAddressesUseCase
import com.pht.vntechpc.domain.usecase.address.GetDefaultAddressUseCase
import com.pht.vntechpc.domain.usecase.order.CreateOrderUseCase
import com.pht.vntechpc.domain.usecase.order.GetPaymentMethodsUseCase
import com.pht.vntechpc.domain.usecase.order.GetShippingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
    private val getAddressesUseCase: GetAddressesUseCase,
    private val getShippingUseCase: GetShippingUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: MutableStateFlow<PaymentUiState> = _uiState

    fun setCartItems(cartItems: List<CartItem>) {
        _uiState.value = _uiState.value.copy(cartItems = cartItems)
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = PaymentState.None)
    }

    fun goToSelectAddress() {
        _uiState.value = _uiState.value.copy(page = PaymentStatePage.SelectAddress)
    }

    fun backToConfirmOrder() {
        _uiState.value = _uiState.value.copy(page = PaymentStatePage.ConfirmOrder)
    }

    fun selectAddress(address: Address) {
        _uiState.value = _uiState.value.copy(address = address)
        getShipping(
            _uiState.value.address?.province ?: "",
            _uiState.value.cartItems?.sumOf { it.price } ?: 0)
    }

    fun selectPaymentMethod(code: String) {
        _uiState.value = _uiState.value.copy(paymentMethodCode = code)
    }

    fun getDefaultAddress() {
        _uiState.value = _uiState.value.copy(status = PaymentState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getDefaultAddressUseCase()
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(address = it, status = PaymentState.None)
                getShipping(
                    _uiState.value.address?.province ?: "",
                    _uiState.value.cartItems?.sumOf { it.price } ?: 0)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = PaymentState.Failure(it.message.toString()))
            }
        }
    }

    fun getAddresses() {
        _uiState.value = _uiState.value.copy(status = PaymentState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getAddressesUseCase()
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(addresses = it, status = PaymentState.None)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = PaymentState.Failure(it.message.toString()))
            }
        }
    }

    fun getPaymentMethods() {
        _uiState.value = _uiState.value.copy(status = PaymentState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getPaymentMethodsUseCase()
            }
            result.onSuccess {
                Log.d("BBB", "getPaymentMethods: $it")
                _uiState.value =
                    _uiState.value.copy(
                        paymentMethods = it,
                        status = PaymentState.None,
                        paymentMethodCode = it.first().code
                    )
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = PaymentState.Failure(it.message.toString()))
            }
        }
    }

    fun getShipping(province: String, orderValue: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getShippingUseCase(province, orderValue)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(shipping = it)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = PaymentState.Failure(it.message.toString()))
            }
        }
    }

    fun createOrder() {
        val addressId = _uiState.value.address?.id ?: 0
        val paymentMethod = _uiState.value.paymentMethodCode ?: ""
        _uiState.value = _uiState.value.copy(status = PaymentState.Loading("Đang xử lý..."))

        if (addressId <= 0 || paymentMethod.isEmpty()) return;

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                createOrderUseCase(addressId, paymentMethod, "", "")
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = PaymentState.Success("Đặt hàng thành công"))
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = PaymentState.Failure(it.message.toString()))
            }
        }
    }
}

data class PaymentUiState(
    val cartItems: List<CartItem>? = null,
    val address: Address? = null,
    val addresses: List<Address> = emptyList(),
    val paymentMethodCode: String? = null,
    val page: PaymentStatePage = PaymentStatePage.ConfirmOrder,
    val status: PaymentState = PaymentState.None,
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val shipping: Shipping? = null
)

sealed class PaymentStatePage {
    object SelectAddress : PaymentStatePage()
    object ConfirmOrder : PaymentStatePage()
}

sealed class PaymentState {
    object None : PaymentState()
    data class Loading(val message: String) : PaymentState()
    data class Success(val message: String) : PaymentState()
    data class Failure(val message: String) : PaymentState()
}