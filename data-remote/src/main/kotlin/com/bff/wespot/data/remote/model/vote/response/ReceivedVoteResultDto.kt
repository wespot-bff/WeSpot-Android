package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.ReceivedVoteResult
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedVoteResultDto(
    val isNew: Boolean,
    val voteCount: Int,
    val voteOption: VoteOptionDto
) {
    fun toReceivedVoteResult(): ReceivedVoteResult {
        return ReceivedVoteResult(
            isNew = isNew,
            voteCount = voteCount,
            voteOption = voteOption.toVoteOption()
        )
    }
}