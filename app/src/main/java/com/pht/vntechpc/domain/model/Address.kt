package com.pht.vntechpc.domain.model

import android.os.Parcelable
import com.pht.vntechpc.data.remote.model.request.AddressRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Int,
    val recipientName: String,
    val phoneNumber: String,
    val province: String,
    val district: String,
    val ward: String,
    val addressDetail: String,
    val isDefault: Boolean
) : Parcelable {
    constructor(
        recipientName: String,
        phoneNumber: String,
        province: String,
        district: String,
        ward: String,
        addressDetail: String,
        isDefault: Boolean = false
    ) : this(
        id = -1,
        recipientName = recipientName,
        phoneNumber = phoneNumber,
        province = province,
        district = district,
        ward = ward,
        addressDetail = addressDetail,
        isDefault = isDefault
    )

    fun toAddressRequest(): AddressRequest {
        return AddressRequest(
            recipientName = recipientName,
            phoneNumber = phoneNumber,
            province = province,
            district = district,
            ward = ward,
            addressDetail = addressDetail,
            isDefault = isDefault
        )
    }

    fun copy(
        recipientName: String = this.recipientName,
        phoneNumber: String = this.phoneNumber,
        province: String = this.province,
        district: String = this.district,
        ward: String = this.ward,
        addressDetail: String = this.addressDetail,
        isDefault: Boolean = this.isDefault,
    ): Address {
        return Address(
            id = id,
            recipientName = recipientName,
            phoneNumber = phoneNumber,
            province = province,
            district = district,
            ward = ward,
            addressDetail = addressDetail,
            isDefault = isDefault
        )
    }
}