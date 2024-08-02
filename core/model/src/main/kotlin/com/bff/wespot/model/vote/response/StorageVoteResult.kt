package com.bff.wespot.model.vote.response

abstract class StorageVoteResult(
    open val voteOption: VoteOption,
    open val voteCount: Int,
)
