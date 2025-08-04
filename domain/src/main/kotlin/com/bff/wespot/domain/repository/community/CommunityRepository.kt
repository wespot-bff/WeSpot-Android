package com.bff.wespot.domain.repository.community

import androidx.paging.PagingData
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.chip.BaseChip
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getCommunityChips(): Result<List<BaseChip>>
    fun getCommunityContentStream(): Flow<PagingData<BaseCommunityContent>>
}
