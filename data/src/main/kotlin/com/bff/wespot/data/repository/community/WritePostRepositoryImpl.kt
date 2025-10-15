package com.bff.wespot.data.repository.community

import com.bff.wespot.data.mapper.community.toDto
import com.bff.wespot.data.remote.source.community.WritePostDataSource
import com.bff.wespot.domain.repository.community.WritePostRepository
import com.bff.wespot.model.community.PostInfo
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

    override suspend fun createPost(info: PostInfo): Boolean {
        val result = dataSource.createPost(info.toDto())

        return result.isSuccess
    }
}