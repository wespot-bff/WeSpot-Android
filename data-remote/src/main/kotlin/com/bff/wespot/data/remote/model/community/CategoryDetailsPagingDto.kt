package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.ImageTypeDto
import com.bff.wespot.model.community.CategoryDetailsPaging
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDetailsPagingDto(
    val data: List<BaseCommunityContentDto>,
    val lastCursorId: Int?,
    val hasNext: Boolean,
    val background: ImageTypeDto,
    val thumbnail: ImageTypeDto
) {
    fun toCategoryDetailsPaging() = CategoryDetailsPaging(
        data = data.map { it.toDomain() },
        lastCursorId = lastCursorId ?: -1,
        hasNext = hasNext,
        background = background.toDomain(),
        thumbnail = background.toDomain(),
    )
}