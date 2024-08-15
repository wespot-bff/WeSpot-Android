package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteReceived
import kotlinx.serialization.Serializable

@Serializable
data class VoteReceivedDto(
    val voteData: List<ReceivedVoteDataDto>,
    val lastCursorId: Int,
    val hasNext: Boolean
) {
    fun toVoteReceived(): VoteReceived {
        return VoteReceived(
            data = voteData.map { it.toVoteData() },
            lastCursorId = lastCursorId,
            hasNext = hasNext
        )
    }
}