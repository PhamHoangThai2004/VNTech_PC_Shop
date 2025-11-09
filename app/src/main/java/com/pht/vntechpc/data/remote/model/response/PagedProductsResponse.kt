package com.pht.vntechpc.data.remote.model.response

data class PagedProductsResponse (
    val content: List<ProductResponse>,
    val pageable: Pageable,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val first: Boolean,
    val numberOfElements: Int,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val empty: Boolean
)

data class Pageable (
    val pageNumber: Int,
    val pageSize: Int,
    val sort: Sort,
    val offset: Int,
    val paged: Boolean,
    val unpaged: Boolean
)

data class Sort (
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)