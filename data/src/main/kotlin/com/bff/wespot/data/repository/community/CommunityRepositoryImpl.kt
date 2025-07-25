package com.bff.wespot.data.repository.community

import androidx.paging.PagingData
import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.chip.BaseChip
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityDataSource: CommunityDataSource,
    private val communityContentPagingRepository: CommunityContentPagingRepository,
    private val communitySearchPagingRepository: CommunitySearchPagingRepository
) : CommunityRepository {
    override suspend fun getCommunityChips(): Result<List<BaseChip>> =
        communityDataSource.getCommunityChips()
            .mapCatching { chips ->
                chips.map { it.toDomain() }
            }

    override fun getCommunityContentStream(): Flow<PagingData<BaseCommunityContent>> =
        communityContentPagingRepository.fetchResultStream()

    override fun getCommunitySearchStream(keyword: String): Flow<PagingData<BaseCommunityContent>> =
        communitySearchPagingRepository.fetchResultStream(mapOf("keyword" to keyword))
}