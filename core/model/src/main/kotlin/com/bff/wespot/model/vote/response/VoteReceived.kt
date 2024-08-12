package com.bff.wespot.model.vote.response

import com.bff.wespot.model.common.Cursor

data class VoteReceived(
    val voteData: List<ReceivedVoteData>,
    val cursor: Cursor,
)
