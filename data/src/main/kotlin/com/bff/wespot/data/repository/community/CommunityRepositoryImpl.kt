package com.bff.wespot.data.repository.community

import androidx.paging.PagingData
import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CategoryDetailsPaging
import com.bff.wespot.model.community.chip.BaseChip
import com.bff.wespot.model.serverDriven.type.ImageType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityDataSource: CommunityDataSource,
    private val communityContentPagingRepository: CommunityContentPagingRepository,
    private val communitySearchPagingRepository: CommunitySearchPagingRepository,
    private val communityAllPostsPagingRepository: CommunityAllPostsPagingRepository,
    private val categoryPostsPagingRepository: CategoryPostsPagingRepository
) : CommunityRepository {
    override suspend fun getCommunityChips(): Result<List<BaseChip>> =
        communityDataSource.getCommunityChips()
            .mapCatching { chips ->
                chips.map { it.toDomain() }
            }

    override fun getCommunityContentStream(
        target: String,
        inquirySize: Int,
    ): Flow<PagingData<BaseCommunityContent>> =
        communityContentPagingRepository.fetchResultStream(
            mapOf(
                "target" to target,
                "inquiry_size" to inquirySize.toString(),
            )
        )

    override fun getCommunitySearchStream(keyword: String): Flow<PagingData<BaseCommunityContent>> =
        communitySearchPagingRepository.fetchResultStream(mapOf("keyword" to keyword))

    override fun getCommunityAllPostsStream(menuType: String): Flow<PagingData<BaseCommunityContent>> =
        communityAllPostsPagingRepository.fetchResultStream(mapOf("menuType" to menuType))

    override fun getCategoryPostsStream(categoryId: String): Flow<PagingData<BaseCommunityContent>> =
        categoryPostsPagingRepository.fetchResultStream(mapOf("categoryId" to categoryId))
        
    override fun getCategoryPostsStreamWithImages(
        categoryId: String, 
        onImagesLoaded: (ImageType, ImageType) -> Unit
    ): Flow<PagingData<BaseCommunityContent>> {
        categoryPostsPagingRepository.setOnImagesLoadedCallback(onImagesLoaded)
        return categoryPostsPagingRepository.fetchResultStream(mapOf("categoryId" to categoryId))
    }
        
    override suspend fun getCategoryDetails(categoryId: String): Result<CategoryDetailsPaging> =
        communityDataSource.getCategoryPosts(categoryId, null)
            .mapCatching { it.toCategoryDetailsPaging() }

    override suspend fun onLikeClicked(postId: String): Boolean {
        return communityDataSource.onLikeClicked(postId).isSuccess
    }

    override suspend fun onScrapClicked(postId: String): Boolean {
        return communityDataSource.onScrapClicked(postId).isSuccess
    }
}