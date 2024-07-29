package com.bff.wespot.model.vote.response

data class SentVoteResult(
    override val voteOption: VoteOption,
    override val voteCount: Int,
) : StorageVoteResult(voteOption, voteCount)
