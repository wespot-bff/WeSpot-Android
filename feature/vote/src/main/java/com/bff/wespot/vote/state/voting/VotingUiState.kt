package com.bff.wespot.vote.state.voting

import com.bff.wespot.model.vote.response.VoteItems

data class VotingUiState(
    val voteItems: VoteItems = VoteItems(emptyList()),
)
