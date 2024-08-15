package com.bff.wespot.model.vote.response

import com.bff.wespot.model.common.Paging


data class VoteSent(
    override val data: List<SentVoteData>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<SentVoteData>
