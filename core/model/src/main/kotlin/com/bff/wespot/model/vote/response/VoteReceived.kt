package com.bff.wespot.model.vote.response

data class VoteReceived(
    val isLastPage: Boolean,
    val voteData: List<VoteData>
)