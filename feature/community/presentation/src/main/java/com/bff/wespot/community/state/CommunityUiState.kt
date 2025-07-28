package com.bff.wespot.community.state

import androidx.paging.PagingData
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import com.bff.wespot.community.uimodel.chip.BaseChipUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CommunityUiState(
    val filterChips: List<BaseChipUiModel> = emptyList(),
    val selectedChipId: String = "",
    val posts: Flow<PagingData<BaseCommunityContentUiModel>> = flow { },
)
