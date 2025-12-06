package com.pht.vntechpc.data.remote.model.request

class AddressRequest (
    val recipientName: String,
    val phoneNumber: String,
    val province: String,
    val district: String,
    val ward: String,
    val addressDetail: String,
    val isDefault: Boolean
)