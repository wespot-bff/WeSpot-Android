package com.bff.wespot.data.remote.model.community

import com.bff.wespot.model.community.CommunityContentPaging
import kotlinx.serialization.Serializable

@Serializable
data class CommunityContentPagingDto(
    val content: List<BaseCommunityContentDto>,
    val lastCursorId: Int,
    val hasNext: Boolean
) {
    fun toCommunityContentPaging() = CommunityContentPaging(
        data = content.map { it.toDomain() },
        lastCursorId = lastCursorId,
        hasNext = hasNext
    )
}