package com.pht.vntechpc.data.remote.service

import com.pht.vntechpc.data.remote.api.CategoryApi
import javax.inject.Inject

class CategoryService @Inject constructor(private val api: CategoryApi) {
    suspend fun fetchCategories() = api.fetchCategories()

    suspend fun fetchCategoryById(categoryId: Int) = api.fetchCategoryById(categoryId)
}