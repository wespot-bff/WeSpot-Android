package com.bff.wespot.data.repository.community

import com.bff.wespot.data.remote.source.community.WritePostDataSource
import com.bff.wespot.domain.repository.community.WritePostRepository
import com.bff.wespot.model.community.chip.CategoryChips
import javax.inject.Inject

class WritePostRepositoryImpl @Inject constructor(
    private val dataSource: WritePostDataSource
) : WritePostRepository {
    override suspend fun getCategories(): Result<List<CategoryChips>> {
        return dataSource.getCategories().mapCatching { chip ->
            chip.map { it.toDomain() }
        }
    }
}