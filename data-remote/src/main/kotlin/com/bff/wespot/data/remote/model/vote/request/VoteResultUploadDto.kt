package com.bff.wespot.data.remote.model.vote.request

import com.bff.wespot.model.vote.request.VoteResultUpload
import kotlinx.serialization.Serializable

@Serializable
data class VoteResultUploadDto(
    val userId: Int,
    val voteOptionId: Int,
) {
    fun toVote() = VoteResultUpload(
        userId = userId,
        voteOptionId = voteOptionId
    )
}