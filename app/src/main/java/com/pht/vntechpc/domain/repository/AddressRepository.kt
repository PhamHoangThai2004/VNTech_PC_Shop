package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.domain.model.Address
import com.pht.vntechpc.domain.model.District
import com.pht.vntechpc.domain.model.Province
import com.pht.vntechpc.domain.model.Ward

interface AddressRepository {
    suspend fun fetchProvinces(): Result<List<Province>>

    suspend fun fetchDistricts(provinceCode: Int): Result<List<District>>

    suspend fun fetchWards(districtCode: Int): Result<List<Ward>>

    suspend fun fetchAddresses(): Result<List<Address>>

    suspend fun fetchDefaultAddress(): Result<Address>

    suspend fun addAddress(address: Address): Result<Address>

    suspend fun updateAddress(addressId: Int, address: Address): Result<Address>

    suspend fun setDefaultAddress(addressId: Int): Result<Address>

    suspend fun removeAddress(addressId: Int): Result<BaseResponse<Unit>>
}