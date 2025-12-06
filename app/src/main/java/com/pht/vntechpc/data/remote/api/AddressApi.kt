package com.pht.vntechpc.data.remote.api

import com.pht.vntechpc.data.remote.model.request.AddressRequest
import com.pht.vntechpc.data.remote.model.response.AddressResponse
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.data.remote.model.response.DistrictResponse
import com.pht.vntechpc.data.remote.model.response.ProvinceResponse
import com.pht.vntechpc.data.remote.model.response.WardResponse
import com.pht.vntechpc.data.remote.network.NoAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AddressApi {
    @NoAuth
    @GET("v1/address/provinces")
    suspend fun fetchProvinces(): Response<BaseResponse<List<ProvinceResponse>>>

    @NoAuth
    @GET("v1/address/districts")
    suspend fun fetchDistricts(@Query("provinceCode") provinceCode: Int): Response<BaseResponse<List<DistrictResponse>>>

    @NoAuth
    @GET("v1/address/wards")
    suspend fun fetchWards(@Query("districtCode") districtCode: Int): Response<BaseResponse<List<WardResponse>>>

    @GET("v1/user/addresses")
    suspend fun fetchAddresses(): Response<BaseResponse<List<AddressResponse>>>

    @GET("v1/user/addresses/default")
    suspend fun fetchDefaultAddress(): Response<BaseResponse<AddressResponse>>

    @POST("v1/user/addresses")
    suspend fun addAddress(@Body request: AddressRequest): Response<BaseResponse<AddressResponse>>

    @PUT("v1/user/addresses/{id}")
    suspend fun updateAddress(
        @Path("id") addressId: Int,
        @Body request: AddressRequest
    ): Response<BaseResponse<AddressResponse>>

    @PUT("v1/user/addresses/{id}/default")
    suspend fun setDefaultAddress(@Path("id") addressId: Int): Response<BaseResponse<AddressResponse>>

    @DELETE("v1/user/addresses/{id}")
    suspend fun removeAddress(@Path("id") addressId: Int): Response<BaseResponse<Unit>>
}