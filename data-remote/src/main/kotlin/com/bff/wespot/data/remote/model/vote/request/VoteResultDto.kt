package com.bff.wespot.data.remote.model.vote.request

import com.bff.wespot.model.vote.request.VoteResult
import kotlinx.serialization.Serializable

@Serializable
data class VoteResultDto(
    val userId: Int,
    val voteOptionId: Int,
) {
    fun toVote() = VoteResult(
            userId = userId,
            voteOptionId = voteOptionId
        )
}