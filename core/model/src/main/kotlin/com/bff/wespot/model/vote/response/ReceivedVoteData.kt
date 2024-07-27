package com.bff.wespot.model.vote.response

data class ReceivedVoteData(
    override val date: String,
    val receivedVoteResults: List<ReceivedVoteResult>
) : VoteData(date)