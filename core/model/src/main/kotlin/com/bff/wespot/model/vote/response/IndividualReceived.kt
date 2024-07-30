package com.bff.wespot.model.vote.response

import com.bff.wespot.model.user.response.ProfileCharacter

data class IndividualReceived(
    val voteResult: ReceivedResult,
)

data class ReceivedResult(
    val voteOption: VoteOption,
    val user: ReceivedUser,
    val rate: Int,
    val voteCount: Int,
)

data class ReceivedUser(
    val id: Int,
    val name: String,
    val introduction: String,
    val profile: ProfileCharacter,
)