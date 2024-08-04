package com.bff.wespot.model.vote.response

data class IndividualSent(
    val voteResult: SentResult,
)

data class SentResult(
    val voteOption: VoteOption,
    val voteUsers: List<VoteUser>,
)
