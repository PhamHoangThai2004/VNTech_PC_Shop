package com.pht.vntechpc.domain.usecase.category

import com.pht.vntechpc.domain.repository.CategoryRepository
import jakarta.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke() = repository.fetchCategories()
}