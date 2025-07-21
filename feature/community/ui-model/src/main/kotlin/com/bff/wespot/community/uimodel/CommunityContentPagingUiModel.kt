package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.CommunityContentPaging

data class CommunityContentPagingUiModel(
    val data: List<BaseCommunityContentUiModel>,
    val lastCursorId: Int,
    val hasNext: Boolean,
)

fun CommunityContentPaging.toUiModel() =
    CommunityContentPagingUiModel(
        data = data.map { it.toUiModel() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )
