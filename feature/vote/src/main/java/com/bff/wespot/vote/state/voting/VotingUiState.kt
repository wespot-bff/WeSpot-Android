package com.bff.wespot.vote.state.voting

import com.bff.wespot.model.vote.response.VoteItem

data class VotingUiState(
    val voteItems: List<VoteItem> = emptyList(),
    val pageNumber: Int = 1,
    val totalPage: Int = 1,
    val currentVote: VoteItem = VoteItem()
)
