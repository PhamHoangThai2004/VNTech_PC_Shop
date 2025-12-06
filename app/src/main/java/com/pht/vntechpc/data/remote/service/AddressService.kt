package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.AddressApi
import com.pht.vntechpc.data.remote.model.request.AddressRequest
import jakarta.inject.Inject

class AddressService @Inject constructor(private val api: AddressApi) {
    suspend fun fetchProvinces() = api.fetchProvinces()

    suspend fun fetchDistricts(provinceCode: Int) = api.fetchDistricts(provinceCode)

    suspend fun fetchWards(districtCode: Int) = api.fetchWards(districtCode)

    suspend fun fetchAddresses() = api.fetchAddresses()

    suspend fun fetchDefaultAddress() = api.fetchDefaultAddress()

    suspend fun addAddress(request: AddressRequest) = api.addAddress(request)

    suspend fun updateAddress(addressId: Int, request: AddressRequest) = api.updateAddress(addressId, request)

    suspend fun setDefaultAddress(addressId: Int) = api.setDefaultAddress(addressId)

    suspend fun removeAddress(addressId: Int) = api.removeAddress(addressId)
}