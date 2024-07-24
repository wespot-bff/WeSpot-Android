package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.VoteItems
import kotlinx.serialization.Serializable

@Serializable
data class VoteItemsDto(
    val voteItems: List<VoteItemDto>
) {
    fun toVoteItems() = VoteItems(
        voteItems = voteItems.map { it.toVoteItem() }
    )
}
