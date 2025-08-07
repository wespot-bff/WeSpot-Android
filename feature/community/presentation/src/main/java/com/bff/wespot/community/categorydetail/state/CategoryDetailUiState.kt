package com.bff.wespot.community.categorydetail.state

import androidx.paging.PagingData
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import com.bff.wespot.model.community.chip.CategoryChips
import com.bff.wespot.model.community.chip.CategoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CategoryDetailUiState(
    val posts: Flow<PagingData<BaseCommunityContentUiModel>> = flow { },
    val likedPosts: Set<String> = emptySet(),
    val scrappedPosts: Set<String> = emptySet(),
    val isRefreshing: Boolean = false,
    val categories: List<CategoryChips> = emptyList(),
    val currentCategory: CategoryItem = CategoryItem.EMPTY,
)
