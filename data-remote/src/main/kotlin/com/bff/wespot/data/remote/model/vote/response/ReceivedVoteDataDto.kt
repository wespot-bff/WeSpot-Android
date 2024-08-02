package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.ReceivedVoteData
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedVoteDataDto(
    val date: String,
    val receivedVoteResults: List<ReceivedVoteResultDto>
) {
    fun toVoteData(): ReceivedVoteData {
        return ReceivedVoteData(
            date = date,
            receivedVoteResults = receivedVoteResults.map { it.toReceivedVoteResult() }
        )
    }
}