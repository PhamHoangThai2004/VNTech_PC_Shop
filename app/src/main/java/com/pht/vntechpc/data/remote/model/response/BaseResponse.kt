package com.pht.vntechpc.data.remote.model.response

class BaseResponse<T> (
    val success: Boolean,
    val message: String,
    val data: T?
)