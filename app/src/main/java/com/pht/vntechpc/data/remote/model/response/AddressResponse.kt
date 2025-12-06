package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Address

class AddressResponse(
    val id: Int,
    val recipientName: String,
    val phoneNumber: String,
    val province: String,
    val district: String,
    val ward: String,
    val addressDetail: String,
    val isDefault: Boolean
)

fun AddressResponse.toAddress(): Address {
    return Address(
        id,
        recipientName,
        phoneNumber,
        province,
        district,
        ward,
        addressDetail,
        isDefault
    )
}