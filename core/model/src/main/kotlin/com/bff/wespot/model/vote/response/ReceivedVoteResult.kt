package com.bff.wespot.model.vote.response

data class ReceivedVoteResult(
    val isNew: Boolean,
    override val voteOption: VoteOption,
    override val voteCount: Int,
) : StorageVoteResult (voteOption, voteCount)