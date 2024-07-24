package com.bff.wespot.vote.state.result

import com.bff.wespot.model.vote.response.VoteResults

data class ResultUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val voteResults: VoteResults = VoteResults(emptyList())
)
