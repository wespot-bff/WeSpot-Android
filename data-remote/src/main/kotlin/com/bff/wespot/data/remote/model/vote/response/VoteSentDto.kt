package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteSent
import kotlinx.serialization.Serializable

@Serializable
data class VoteSentDto(
    val voteData: List<SentVoteDataDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toVoteSent(): VoteSent {
        return VoteSent(
            data = voteData.map { it.toSentVoteData() },
            lastCursorId = lastCursorId,
            hasNext = hasNext
        )
    }
}