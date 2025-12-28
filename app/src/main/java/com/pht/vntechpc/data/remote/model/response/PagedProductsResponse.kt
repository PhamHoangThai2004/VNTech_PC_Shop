package com.pht.vntechpc.data.remote.model.response

data class PagedProductsResponse (
    val content: List<ProductResponse>,
    val totalPages: Int,
    val totalElements: Int,
    val page: Int,
    val size: Int,
    val number: Int,
)