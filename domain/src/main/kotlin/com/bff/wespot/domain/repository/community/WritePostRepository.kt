package com.bff.wespot.domain.repository.community

import com.bff.wespot.model.community.chip.CategoryChips

interface WritePostRepository {
    suspend fun getCategories(): Result<List<CategoryChips>>
}
