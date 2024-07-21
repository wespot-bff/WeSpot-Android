package com.bff.wespot.model.vote.response

data class VoteItem(
    val voteUser: VoteUser,
    val voteOption: List<VoteOption>,
) {
    constructor() : this(VoteUser(), emptyList())
}
