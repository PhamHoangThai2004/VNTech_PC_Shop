package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.toAddress
import com.pht.vntechpc.data.remote.model.response.toDistrict
import com.pht.vntechpc.data.remote.model.response.toProvince
import com.pht.vntechpc.data.remote.model.response.toWard
import com.pht.vntechpc.data.remote.service.AddressService
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.model.District
import com.pht.vntechpc.domain.model.Province
import com.pht.vntechpc.domain.model.Ward
import com.pht.vntechpc.domain.repository.AddressRepository
import com.pht.vntechpc.domain.repository.BaseRepository
import jakarta.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val service: AddressService
) : AddressRepository, BaseRepository() {
    override suspend fun fetchProvinces(): Result<List<Province>> {
        return apiCall({ service.fetchProvinces() }, { it -> it.map { it.toProvince() } })
    }

    override suspend fun fetchDistricts(provinceCode: Int): Result<List<District>> {
        return apiCall(
            { service.fetchDistricts(provinceCode) },
            { it -> it.map { it.toDistrict() } })
    }

    override suspend fun fetchWards(districtCode: Int): Result<List<Ward>> {
        return apiCall({ service.fetchWards(districtCode) }, { it -> it.map { it.toWard() } })
    }

    override suspend fun fetchAddresses(): Result<List<Address>> {
        return apiCall({ service.fetchAddresses() }, { it -> it.map { it.toAddress() } })
    }

    override suspend fun fetchDefaultAddress(): Result<Address> {
        return apiCall({ service.fetchDefaultAddress() }, { it.toAddress() })
    }

    override suspend fun addAddress(address: Address): Result<Address> {
        val request = address.toAddressRequest()
        return apiCall({ service.addAddress(request) }, { it.toAddress() })
    }

    override suspend fun updateAddress(addressId: Int, address: Address): Result<Address> {
        val request = address.toAddressRequest()
        return apiCall({ service.updateAddress(addressId, request) }, { it.toAddress() })
    }

    override suspend fun setDefaultAddress(addressId: Int): Result<Address> {
        return apiCall({ service.setDefaultAddress(addressId) }, { it.toAddress() })
    }

    override suspend fun removeAddress(addressId: Int): Result<BaseResponse<Unit>> {
        return apiCallNoData { service.removeAddress(addressId) }
    }
}