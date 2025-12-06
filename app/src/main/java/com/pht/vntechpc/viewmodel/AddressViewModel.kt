package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.usecase.address.GetAddressesUseCase
import com.pht.vntechpc.domain.usecase.address.SetDefaultAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val setDefaultAddressUseCase: SetDefaultAddressUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState

    fun resetState() {
        _uiState.value = _uiState.value.copy(status = AddressState.None)
    }

    fun getAddresses() {
        _uiState.value.copy(status = AddressState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getAddressesUseCase()
            }
            result.onSuccess {
                Log.d("BBB", "Get Addresses Success: $it")
                _uiState.value = _uiState.value.copy(addresses = it, status = AddressState.None)
            }.onFailure {
                Log.d("BBB", "Get Addresses Faults: ${it.message}")
                _uiState.value =
                    _uiState.value.copy(status = AddressState.Failure(it.message.toString()))
            }
        }
    }

    fun setDefaultAddress(addressId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                setDefaultAddressUseCase(addressId)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = AddressState.None)
                getAddresses()
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressState.Failure(it.message.toString()))
            }
        }
    }
}

data class AddressUiState(
    val addresses: List<Address> = emptyList(),
    val status: AddressState = AddressState.None
)

sealed class AddressState {
    object None : AddressState()
    data class Loading(val message: String) : AddressState()
    data class Failure(val message: String) : AddressState()
}