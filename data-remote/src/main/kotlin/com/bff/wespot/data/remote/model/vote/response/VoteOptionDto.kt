package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteOption
import kotlinx.serialization.Serializable

@Serializable
data class VoteOptionDto(
    val id: Int,
    val content: String
) {
    fun toVoteOption() = VoteOption(
            id = id,
            content = content
        )
}