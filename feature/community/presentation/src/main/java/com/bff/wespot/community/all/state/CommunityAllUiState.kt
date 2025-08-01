package com.bff.wespot.community.all.state

import androidx.paging.PagingData
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CommunityAllUiState(
    val paging: Flow<PagingData<BaseCommunityContentUiModel>> = flow { },
    val likedPosts: Set<String> = emptySet(),
    val scrappedPosts: Set<String> = emptySet(),
)
