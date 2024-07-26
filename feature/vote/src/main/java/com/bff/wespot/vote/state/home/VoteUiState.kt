package com.bff.wespot.vote.state.home

import com.bff.wespot.model.vote.response.VoteResult

data class VoteUiState(
    val voteResults: List<VoteResult> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
)
