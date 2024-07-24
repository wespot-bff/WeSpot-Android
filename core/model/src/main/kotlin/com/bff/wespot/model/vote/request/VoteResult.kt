package com.bff.wespot.model.vote.request

data class VoteResult(
    val userId: Int,
    val voteOptionId: Int,
) {
    constructor() : this(0, 0)
}
