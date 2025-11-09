package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Category

data class CategoryResponse (
    val id: String,
    val categoryName: String,
    val createdAt: String,
    val updatedAt: String
)

fun CategoryResponse.toCategory(): Category {
    return Category(id, categoryName, createdAt, updatedAt)
}