package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.chip.CategoryChipsDto

interface WritePostDataSource {
    suspend fun getCategories(): Result<List<CategoryChipsDto>>
}