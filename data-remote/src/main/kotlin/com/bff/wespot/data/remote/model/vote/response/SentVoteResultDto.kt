package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.SentVoteResult
import kotlinx.serialization.Serializable

@Serializable
data class SentVoteResultDto(
    val voteOption: VoteOptionDto,
    val voteCount: Int,
) {
    fun toSentVoteResult(): SentVoteResult {
        return SentVoteResult(
            voteOption = voteOption.toVoteOption(),
            voteCount = voteCount
        )
    }
}