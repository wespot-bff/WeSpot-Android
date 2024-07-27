package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteReceived
import kotlinx.serialization.Serializable

@Serializable
data class VoteReceivedDto(
    val voteData: List<VoteDataDto>,
    val isLastPage: Boolean,
) {
    fun toVoteReceived(): VoteReceived {
        return VoteReceived(
            isLastPage = isLastPage,
            voteData = voteData.map { it.toVoteData() }
        )
    }
}