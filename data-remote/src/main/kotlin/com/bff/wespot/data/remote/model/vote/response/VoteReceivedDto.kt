package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.common.Cursor
import com.bff.wespot.model.vote.response.VoteReceived
import kotlinx.serialization.Serializable

@Serializable
data class VoteReceivedDto(
    val voteData: List<ReceivedVoteDataDto>,
    val hasNext: Boolean,
    val lastCursorId: Int,
) {
    fun toVoteReceived(): VoteReceived {
        return VoteReceived(
            voteData = voteData.map { it.toVoteData() },
            cursor = Cursor(
                hasNext = hasNext,
                lastCursorId = lastCursorId
            )
        )
    }
}