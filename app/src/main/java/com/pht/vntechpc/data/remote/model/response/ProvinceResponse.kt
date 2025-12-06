package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Province

data class ProvinceResponse (
    val code: Int,
    val name: String,
    val type: String
)

fun ProvinceResponse.toProvince(): Province {
    return Province(code, name, type)
}