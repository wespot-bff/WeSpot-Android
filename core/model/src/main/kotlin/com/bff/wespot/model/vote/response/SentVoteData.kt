package com.bff.wespot.model.vote.response

data class SentVoteData(
    override val date: String,
    val sentVoteResults: List<SentVoteResult>,
) : VoteData(date)
