package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.data.remote.model.response.toProduct
import com.pht.vntechpc.domain.usecase.category.GetCategoriesUseCase
import com.pht.vntechpc.domain.usecase.category.GetCategoryDetailUseCase
import com.pht.vntechpc.domain.usecase.product.GetProductDetailUseCase
import com.pht.vntechpc.domain.usecase.product.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {

    fun getAllCategory() {
        viewModelScope.launch {
            val result = getCategoriesUseCase()
            result.onSuccess {
                for (category in it) {
                    Log.d("BBB", "Category: $category")
                }
            }.onFailure {
                Log.d("BBB", "Get all category failure: ${it.message}")
            }
        }
    }

    fun getCategoryById(categoryId: Int) {
        viewModelScope.launch {
            val result = getCategoryDetailUseCase(categoryId)
            result.onSuccess {
                Log.d("BBB", "Category: $it")
            }.onFailure {
                Log.d("BBB", "Get category by id failure: ${it.message}")
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            val result = getProductsUseCase()
            result.onSuccess {
                val response = it.content
                val products = response.map { it -> it.toProduct() }
                for (product in products) {
                    Log.d("BBB", "Product: $product")
                }
            }.onFailure {
                Log.d("BBB", "Get all products failure: ${it.message}")
            }
        }
    }

    fun getProductById(productId: Int) {
        viewModelScope.launch {
            val result = getProductDetailUseCase(productId)
            result.onSuccess {
                Log.d("BBB", "Product: $it")
            }.onFailure {
                Log.d("BBB", "Get product by id failure: ${it.message}")
            }
        }
    }
}