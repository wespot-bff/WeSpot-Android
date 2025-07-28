package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.CommunityContentPagingDto
import com.bff.wespot.data.remote.model.community.chip.BaseChipDto

interface CommunityDataSource {
    suspend fun getCommunityChips(): Result<List<BaseChipDto>>
    suspend fun getCommunityContent(
        target: String,
        inquirySize: Int,
        cursorId: Int?
    ): Result<CommunityContentPagingDto>

    suspend fun getCommunitySearchContent(
        keyword: String,
        cursorId: Int?
    ): Result<CommunityContentPagingDto>
}