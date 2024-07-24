package com.bff.wespot.data.remote.model.vote.request

import com.bff.wespot.model.vote.request.VoteResults
import kotlinx.serialization.Serializable

@Serializable
data class VoteResultsDto(
    val voteResultDtos: List<VoteResultDto>
) {
    fun toVotes() = VoteResults(
        voteResults = voteResultDtos.map { it.toVote() }
    )
}