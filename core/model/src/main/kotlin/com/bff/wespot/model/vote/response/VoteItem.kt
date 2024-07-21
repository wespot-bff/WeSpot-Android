package com.bff.wespot.model.vote.response

data class VoteItem(
    val user: User,
    val voteOption: List<VoteOption>,
)
