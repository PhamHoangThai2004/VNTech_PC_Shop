package com.pht.vntechpc.domain.model

data class District(
    val code: Int,
    val name: String,
    val type: String,
    val parentCode: Int
)