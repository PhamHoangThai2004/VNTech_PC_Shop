package com.pht.vntechpc.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.model.District
import com.pht.vntechpc.domain.model.Province
import com.pht.vntechpc.domain.model.Ward
import com.pht.vntechpc.domain.usecase.address.AddNewAddressUseCase
import com.pht.vntechpc.domain.usecase.address.GetDistrictsUseCase
import com.pht.vntechpc.domain.usecase.address.GetProvincesUseCase
import com.pht.vntechpc.domain.usecase.address.GetWardsUseCase
import com.pht.vntechpc.domain.usecase.address.RemoveAddressUseCase
import com.pht.vntechpc.domain.usecase.address.UpdateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddressFormViewModel @Inject constructor(
    private val getProvincesUseCase: GetProvincesUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val getWardsUseCase: GetWardsUseCase,
    private val addNewAddressUseCase: AddNewAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val removeAddressUseCase: RemoveAddressUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressFormUiState())
    val uiState: MutableStateFlow<AddressFormUiState> = _uiState

    fun updateRecipientName(recipientName: String) {
        when {
            recipientName.trim().isEmpty() -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(recipientName = recipientName),
                    isRecipientNameValid = false,
                    recipientNameError = "Yêu cầu nhập tên người nhận"
                )
            }

            recipientName.trim().length > 255 -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(recipientName = recipientName),
                    isRecipientNameValid = false,
                    recipientNameError = "Tên quá dài"
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(recipientName = recipientName),
                    isRecipientNameValid = true,
                    recipientNameError = ""
                )
            }
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        val phoneRegex = Regex("^(0|\\+84)(\\d{9})$")

        when {
            phoneNumber.trim().isEmpty() -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(phoneNumber = phoneNumber),
                    isPhoneNumberValid = false,
                    phoneNumberError = "Yêu cầu nhập số điện thoại"
                )
            }

            !phoneRegex.matches(phoneNumber) -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(phoneNumber = phoneNumber),
                    isPhoneNumberValid = false,
                    phoneNumberError = "Số điện thoại không hợp lệ"
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(phoneNumber = phoneNumber),
                    isPhoneNumberValid = true, phoneNumberError = ""
                )
            }
        }
    }

    fun updateAddressDetail(addressDetail: String) {
        when {
            addressDetail.trim().isEmpty() -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(addressDetail = addressDetail),
                    isAddressDetailValid = false,
                    addressDetailError = "Yêu cầu nhập địa chỉ chi tiết"
                )
            }

            addressDetail.trim().length > 255 -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(addressDetail = addressDetail),
                    isAddressDetailValid = false,
                    addressDetailError = "Địa chỉ quá dài"
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    address = _uiState.value.address.copy(addressDetail = addressDetail),
                    isAddressDetailValid = true,
                    addressDetailError = ""
                )
            }
        }
    }

    fun setAddress(address: Address) {
        _uiState.value = _uiState.value.copy(
            isEdit = true,
            address = address,
            location = "${address.province}, ${address.district}, ${address.ward}"
        )
    }

    fun setState() {
        _uiState.value = _uiState.value.copy(status = AddressFormState.None)
    }

    fun setIsDefault() {
        _uiState.value = _uiState.value.copy(
            address = _uiState.value.address.copy(isDefault = !_uiState.value.address.isDefault)
        )
    }

    fun backToAddressForm() {
        _uiState.value = _uiState.value.copy(page = AddressFormStatePage.AddressForm)
    }

    fun goToAddressLocationPicker() {
        _uiState.value = _uiState.value.copy(page = AddressFormStatePage.AddressLocationPicker)
    }

    fun selectedProvince(provinceName: String) {
        if (provinceName != _uiState.value.address.province) {
            _uiState.value =
                _uiState.value.copy(
                    address = _uiState.value.address.copy(
                        province = provinceName,
                        district = "Chọn quận/huyện",
                        ward = "Chọn phường/xã"
                    )
                )
            val provinceCode = _uiState.value.provinces.first { it.name == provinceName }.code
            getDistricts(provinceCode)
        }
    }

    fun selectedDistrict(districtName: String) {
        if (districtName != _uiState.value.address.district) {
            _uiState.value =
                _uiState.value.copy(
                    address = _uiState.value.address.copy(
                        district = districtName,
                        ward = "Chọn phường/xã"
                    )
                )
            val districtCode = _uiState.value.districts.first { it.name == districtName }.code
            getWards(districtCode)
        }
    }

    fun selectedWard(wardName: String) {
        if (wardName != _uiState.value.address.ward) {
            _uiState.value =
                _uiState.value.copy(address = _uiState.value.address.copy(ward = wardName))
        }
    }

    fun getLocation(address: Address) {
        if (address.province == "Chọn tỉnh/thành phố" || address.district == "Chọn quận/huyện" || address.ward == "Chọn phường/xã") {
            _uiState.value = _uiState.value.copy(
                isLocationValid = false,
                status = AddressFormState.Failure("Vui lòng chọn đầy đủ địa chỉ")
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            page = AddressFormStatePage.AddressForm,
            isLocationValid = true,
            location = "${_uiState.value.address.province}, ${_uiState.value.address.district}, ${_uiState.value.address.ward}"
        )
    }

    fun getProvinces() {
        _uiState.value = _uiState.value.copy(status = AddressFormState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getProvincesUseCase()
            }
            result.onSuccess {
                _uiState.value =
                    _uiState.value.copy(provinces = it, status = AddressFormState.None)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressFormState.Failure(it.message.toString()))
            }
        }
    }

    fun getDistricts(provinceCode: Int) {
        _uiState.value = _uiState.value.copy(status = AddressFormState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getDistrictsUseCase(provinceCode)
            }
            result.onSuccess {
                _uiState.value =
                    _uiState.value.copy(districts = it, status = AddressFormState.None)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressFormState.Failure(it.message.toString()))
            }
        }
    }

    fun getWards(districtCode: Int) {
        _uiState.value = _uiState.value.copy(status = AddressFormState.Loading("Đang tải..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getWardsUseCase(districtCode)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(wards = it, status = AddressFormState.None)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressFormState.Failure(it.message.toString()))
            }
        }
    }

    fun addNewAddress(address: Address) {
        updateAddressDetail(address.addressDetail)
        updatePhoneNumber(address.phoneNumber)
        updateRecipientName(address.recipientName)
        if (!_uiState.value.isAddressDetailValid || !_uiState.value.isPhoneNumberValid
            || !_uiState.value.isRecipientNameValid || !_uiState.value.isLocationValid
        ) return

        _uiState.value = _uiState.value.copy(status = AddressFormState.Loading("Đang xử lý..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                addNewAddressUseCase(address)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = AddressFormState.Success)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressFormState.Failure(it.message.toString()))
            }
        }
    }

    fun updateAddress(
        address: Address,
    ) {
        updateAddressDetail(address.addressDetail)
        updatePhoneNumber(address.phoneNumber)
        updateRecipientName(address.recipientName)
        if (!_uiState.value.isAddressDetailValid || !_uiState.value.isPhoneNumberValid
            || !_uiState.value.isRecipientNameValid || !_uiState.value.isLocationValid
        ) {
            return
        }
        _uiState.value = _uiState.value.copy(status = AddressFormState.Loading("Đang cập nhập..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                updateAddressUseCase(address.id, address)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = AddressFormState.Success)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressFormState.Failure(it.message.toString()))
            }
        }
    }

    fun confirmRemoveAddress() {
        _uiState.value =
            _uiState.value.copy(status = AddressFormState.ConfirmRemoveAddress("Xác nhận xoá địa chỉ này?"))
    }

    fun removeAddress(addressId: Int) {
        if (addressId < 0) return
        _uiState.value = _uiState.value.copy(status = AddressFormState.Loading("Đang xóa..."))
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                removeAddressUseCase(addressId)
            }
            result.onSuccess {
                _uiState.value = _uiState.value.copy(status = AddressFormState.Success)
            }.onFailure {
                _uiState.value =
                    _uiState.value.copy(status = AddressFormState.Failure(it.message.toString()))
            }
        }
    }
}

data class AddressFormUiState(
    val isEdit: Boolean = false,
    val address: Address = Address(
        recipientName = "",
        phoneNumber = "",
        province = "Chọn tỉnh/thành phố",
        district = "Chọn quận/huyện",
        ward = "Chọn phường/xã",
        addressDetail = "",
        isDefault = false
    ),
    val recipientNameError: String = "",
    val phoneNumberError: String = "",
    val addressDetailError: String = "",
    val isRecipientNameValid: Boolean = true,
    val isPhoneNumberValid: Boolean = true,
    val isAddressDetailValid: Boolean = true,
    val isLocationValid: Boolean = true,
    val provinces: List<Province> = emptyList(),
    val districts: List<District> = emptyList(),
    val wards: List<Ward> = emptyList(),
    val location: String = "",
    val page: AddressFormStatePage = AddressFormStatePage.AddressForm,
    val status: AddressFormState = AddressFormState.None
)

sealed class AddressFormStatePage {
    object AddressForm : AddressFormStatePage()
    object AddressLocationPicker : AddressFormStatePage()
}

sealed class AddressFormState {
    object None : AddressFormState()
    data class Loading(val message: String) : AddressFormState()
    data class ConfirmRemoveAddress(val message: String) : AddressFormState()
    object Success : AddressFormState()
    data class Failure(val message: String) : AddressFormState()
}
