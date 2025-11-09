package com.pht.vntechpc.data.repository

import com.pht.vntechpc.data.remote.model.response.toCategory
import com.pht.vntechpc.data.remote.service.CategoryService
import com.pht.vntechpc.domain.model.Category
import com.pht.vntechpc.domain.repository.BaseRepository
import com.pht.vntechpc.domain.repository.CategoryRepository
import jakarta.inject.Inject


class CategoryRepositoryImpl @Inject constructor(
    private val service: CategoryService
) : BaseRepository(), CategoryRepository {
    override suspend fun fetchCategories(): Result<List<Category>> {
        return apiCall({ service.fetchCategories() }, { it -> it.map { it.toCategory() } })
    }

    override suspend fun fetchCategoryById(categoryId: Int): Result<Category> {
        return apiCall({ service.fetchCategoryById(categoryId) }, { it.toCategory() })
    }
}