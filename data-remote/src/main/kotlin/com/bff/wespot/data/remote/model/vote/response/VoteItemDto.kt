package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteItem
import kotlinx.serialization.Serializable

@Serializable
data class VoteItemDto(
    val user: VoteUserDto,
    val voteOptions: List<VoteOptionDto>
) {
    fun toVoteItem() = VoteItem(
            voteUser = user.toVoteUser(),
            voteOption = voteOptions.map { it.toVoteOption() }
        )
}