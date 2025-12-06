package com.pht.vntechpc.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.pht.vntechpc.domain.model.Ward

data class WardResponse (
    val code: Int,
    val name: String,
    val type: String,
    @SerializedName("parent_code")
    val parentCode: Int,
    @SerializedName("path_with_type")
    val pathWithType: String
)

fun WardResponse.toWard(): Ward {
    return Ward(code, name, type, parentCode, pathWithType)
}