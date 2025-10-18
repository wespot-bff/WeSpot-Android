package com.bff.wespot.domain.repository.community

import androidx.paging.PagingData
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CategoryDetailsPaging
import com.bff.wespot.model.community.chip.BaseChip
import com.bff.wespot.model.serverDriven.type.ImageType
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getCommunityChips(): Result<List<BaseChip>>
    fun getCommunityContentStream(
        target: String,
        inquirySize: Int,
    ): Flow<PagingData<BaseCommunityContent>>

    fun getCommunitySearchStream(keyword: String): Flow<PagingData<BaseCommunityContent>>

    fun getCommunityAllPostsStream(menuType: String): Flow<PagingData<BaseCommunityContent>>

    fun getCategoryPostsStream(categoryId: String): Flow<PagingData<BaseCommunityContent>>

    fun getCategoryPostsStreamWithImages(
        categoryId: String,
        onImagesLoaded: (ImageType, ImageType) -> Unit,
    ): Flow<PagingData<BaseCommunityContent>>

    suspend fun getCategoryDetails(categoryId: String): Result<CategoryDetailsPaging>

    suspend fun onLikeClicked(postId: String): Boolean

    suspend fun onScrapClicked(postId: String): Boolean
}
