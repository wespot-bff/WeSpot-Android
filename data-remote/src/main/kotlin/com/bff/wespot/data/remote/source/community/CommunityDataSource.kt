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

    suspend fun getCommunityPostsByType(
        menuType: String,
        cursorId: Int?
    ): Result<CommunityContentPagingDto>

    suspend fun getCategoryPosts(
        categoryId: String,
        cursorId: Int?
    ): Result<CommunityContentPagingDto>

    suspend fun onLikeClicked(postId: String): Result<Unit>

    suspend fun onScrapClicked(postId: String): Result<Unit>
}