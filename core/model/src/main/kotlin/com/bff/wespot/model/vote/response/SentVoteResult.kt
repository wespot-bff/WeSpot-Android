package com.bff.wespot.model.vote.response

data class SentVoteStorage(
    val vote: SentVoteResult,
)

data class SentVoteResult(
    override val voteOption: VoteOption,
    override val voteCount: Int,
    val user: VoteProfile,
) : StorageVoteResult(voteOption, voteCount)
