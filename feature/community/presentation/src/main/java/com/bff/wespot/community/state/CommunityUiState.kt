package com.bff.wespot.community.state

import androidx.paging.PagingData
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import com.bff.wespot.community.uimodel.chip.BaseChipUiModel
import com.bff.wespot.model.community.chip.CategoryChips
import com.bff.wespot.model.community.chip.CategoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CommunityUiState(
    val filterChips: List<BaseChipUiModel> = emptyList(),
    val selectedChipId: String = "",
    val posts: Flow<PagingData<BaseCommunityContentUiModel>> = flow { },
    val likedPosts: Set<String> = emptySet(),
    val scrappedPosts: Set<String> = emptySet(),
    val isRefreshing: Boolean = false,
    val showCategoryBottomSheet: Boolean = false,
    val categories: List<CategoryChips> = emptyList(),
) {
    val selectedChip: CategoryItem = categories
        .flatMap { it.chips }
        .find { it.id == selectedChipId } ?: CategoryItem.EMPTY
}
