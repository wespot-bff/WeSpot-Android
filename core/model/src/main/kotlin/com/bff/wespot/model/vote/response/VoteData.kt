package com.bff.wespot.model.vote.response

data class VoteData(
    val date: String,
    val receivedVoteResults: List<ReceivedVoteResult>
)