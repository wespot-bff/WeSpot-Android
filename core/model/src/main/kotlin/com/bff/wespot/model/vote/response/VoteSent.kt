package com.bff.wespot.model.vote.response

import com.bff.wespot.model.common.Cursor

data class VoteSent(
    val voteData: List<SentVoteData>,
    val cursor: Cursor,
)
