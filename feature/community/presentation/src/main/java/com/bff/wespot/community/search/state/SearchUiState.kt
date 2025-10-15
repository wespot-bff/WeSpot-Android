package com.bff.wespot.community.search.state

import androidx.paging.PagingData
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchUiState(
    val searches: Flow<PagingData<BaseCommunityContentUiModel>> = flow { },
    val keyword: String = "",
)
