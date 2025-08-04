package com.bff.wespot.community.state

import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import com.bff.wespot.community.uimodel.chip.BaseChipUiModel

data class CommunityUiState(
    val filterChips: List<BaseChipUiModel> = emptyList(),
    val selectedChipId: String = "",
    val posts: List<BaseCommunityContentUiModel> = emptyList(),
)
