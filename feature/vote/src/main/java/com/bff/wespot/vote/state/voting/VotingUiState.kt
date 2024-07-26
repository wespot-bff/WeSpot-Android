package com.bff.wespot.vote.state.voting

import com.bff.wespot.model.vote.request.VoteResultUpload
import com.bff.wespot.model.vote.response.VoteItem

data class VotingUiState(
    val voteItems: List<VoteItem> = emptyList(),
    val pageNumber: Int = -1,
    val totalPage: Int = 0,
    val currentVote: VoteItem = VoteItem(),
    val selectedVote: List<VoteResultUpload> = emptyList(),
    val start: Boolean = true,
    val loading: Boolean = false,
)
