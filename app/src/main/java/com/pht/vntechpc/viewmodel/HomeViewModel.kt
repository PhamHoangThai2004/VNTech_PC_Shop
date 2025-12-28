package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.remote.model.response.toProduct
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.domain.usecase.cart.AddProductToCartUseCase
import com.pht.vntechpc.domain.usecase.product.GetProductDetailUseCase
import com.pht.vntechpc.domain.usecase.product.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = HomeState.None)
    }

    fun getAllProducts() {
        _uiState.value = _uiState.value.copy(
            status = HomeState.Loading("Đang tải..."),
            page = 0,
            isLastPage = false
        )
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { getProductsUseCase() }
            result.onSuccess { it ->
                val response = it.content
                val products = response.map { it.toProduct() }
                _uiState.value = _uiState.value.copy(
                    products = products,
                    status = HomeState.None
                )
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = HomeState.Failure(it.message.toString()))
            }
        }
    }

    fun loadMoreProducts() {
        if (_uiState.value.isLoadingMore || _uiState.value.isLastPage) return
        _uiState.value = _uiState.value.copy(isLoadingMore = true)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getProductsUseCase(page = _uiState.value.page + 1)
            }
            result.onSuccess { it ->
                val response = it.content
                val newProducts = response.map { it.toProduct() }
                if (newProducts.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isLastPage = true)
                } else {
                    _uiState.value = _uiState.value.copy(
                        products = _uiState.value.products + newProducts,
                        status = HomeState.None,
                        isLoadingMore = false,
                        page = _uiState.value.page + 1,
                        isLastPage = newProducts.isEmpty()
                    )
                    Log.d(
                        "BBB",
                        "New products: ${_uiState.value.products.size}, and ${_uiState.value.page}"
                    )
                }
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(
                        isLoadingMore = false,
                        status = HomeState.Failure(it.message.toString())
                    )
            }
        }
    }

    fun getProductById(productId: Int) {
        viewModelScope.launch {
            val result = getProductDetailUseCase(productId)
            result.onSuccess {
                Log.d("BBB", "Product: $it")
                _uiState.value =
                    _uiState.value.copy(status = HomeState.GetProductDetailSuccess(it))
            }.onFailure {
                Log.d("BBB", "Get product by id failure: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = HomeState.Failure(it.message.toString()))
            }
        }
    }

    fun addProductToCart(productId: Int, quantity: Int) {
        _uiState.value = _uiState.value.copy(status = HomeState.Loading("Đang xử lý..."))
        viewModelScope.launch {
            val result =
                withContext(Dispatchers.IO) { addProductToCartUseCase(productId, quantity) }
            result.onSuccess {
                _uiState.value =
                    _uiState.value.copy(status = HomeState.AddToCartSuccess("Thêm sản phẩm vào giỏ hàng thành công!"))
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = HomeState.Failure(it.message.toString()))
            }
        }
    }
}

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val status: HomeState = HomeState.None,
    val isLoadingMore: Boolean = false,
    val page: Int = 0,
    val isLastPage: Boolean = false
)

sealed class HomeState {
    object None : HomeState()
    data class Loading(val message: String) : HomeState()
    data class AddToCartSuccess(val message: String) : HomeState()
    data class GetProductDetailSuccess(val product: Product) : HomeState()
    data class Failure(val message: String) : HomeState()
}