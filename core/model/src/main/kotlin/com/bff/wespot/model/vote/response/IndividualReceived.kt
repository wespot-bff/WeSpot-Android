package com.bff.wespot.model.vote.response

data class IndividualReceived(
    val voteResult: ReceivedResult,
)

data class ReceivedResult(
    val voteOption: VoteOption,
    val user: VoteUser,
    val rate: Int,
    val voteCount: Int,
)