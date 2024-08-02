package com.bff.wespot.model.vote.response

import com.bff.wespot.model.user.response.ProfileCharacter

data class IndividualSent(
    val voteResult: SentResult,
)

data class SentResult(
    val voteOption: VoteOption,
    val voteUsers: List<SentUser>,
)

data class SentUser(
    val id: Int,
    val name: String,
    val profile: ProfileCharacter,
)
