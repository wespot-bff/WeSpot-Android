package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteData
import kotlinx.serialization.Serializable

@Serializable
data class VoteDataDto(
    val date: String,
    val receivedVoteResults: List<ReceivedVoteResultDto>
) {
    fun toVoteData(): VoteData {
        return VoteData(
            date = date,
            receivedVoteResults = receivedVoteResults.map { it.toReceivedVoteResult() }
        )
    }
}