package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteItem
import kotlinx.serialization.Serializable

@Serializable
data class VoteItemDto(
    val user: UserDto,
    val voteOptions: List<VoteOptionDto>
) {
    fun toVoteItem() = VoteItem(
            user = user.toUser(),
            voteOption = voteOptions.map { it.toVoteOption() }
        )
}