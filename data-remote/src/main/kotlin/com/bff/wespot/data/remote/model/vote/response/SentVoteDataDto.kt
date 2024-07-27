package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.SentVoteData
import kotlinx.serialization.Serializable

@Serializable
data class SentVoteDataDto(
    val date: String,
    val sentVoteResults: List<SentVoteResultDto>
) {
    fun toSentVoteData(): SentVoteData {
        return SentVoteData(
            date = date,
            sentVoteResults = sentVoteResults.map { it.toSentVoteResult() }
        )
    }
}