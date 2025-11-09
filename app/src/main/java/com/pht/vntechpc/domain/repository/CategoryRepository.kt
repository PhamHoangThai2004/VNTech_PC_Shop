package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.domain.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(): Result<List<Category>>

    suspend fun fetchCategoryById(categoryId: Int): Result<Category>
}