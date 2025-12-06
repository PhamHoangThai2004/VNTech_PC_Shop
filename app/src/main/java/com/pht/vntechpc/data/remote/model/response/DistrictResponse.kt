package com.pht.vntechpc.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.pht.vntechpc.domain.model.District

data class DistrictResponse (
    val code: Int,
    val name: String,
    val type: String,
    @SerializedName("parent_code")
    val parentCode: Int
)

fun DistrictResponse.toDistrict(): District {
    return District(code, name, type, parentCode)
}