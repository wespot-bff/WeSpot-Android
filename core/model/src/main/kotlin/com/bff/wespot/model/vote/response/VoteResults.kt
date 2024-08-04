package com.bff.wespot.model.vote.response

data class VoteResults(
    val voteResults: List<VoteResult>,
)

data class VoteResult(
    val voteOption: VoteOption,
    val results: List<Result>,
)

data class Result(
    val user: VoteUser,
    val voteCount: Int,
)
