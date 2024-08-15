package com.bff.wespot.model.vote.response

import com.bff.wespot.model.common.Paging


data class VoteReceived(
    override val data: List<ReceivedVoteData>,
    override val lastCursorId: Int,
    override val hasNext: Boolean
) : Paging<ReceivedVoteData>
