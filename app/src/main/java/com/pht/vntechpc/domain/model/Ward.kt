package com.pht.vntechpc.domain.model

data class Ward(
    val code: Int,
    val name: String,
    val type: String,
    val parentCode: Int,
    val pathWithType: String
)