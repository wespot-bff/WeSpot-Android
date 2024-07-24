package com.bff.wespot.model.vote.response

import com.bff.wespot.model.user.response.ProfileCharacter

data class VoteResults (
    val voteResults: List<VoteResult>
)

data class VoteResult (
    val voteOption: VoteOption,
    val results: List<Result>,
)

data class Result (
    val user: VoteProfile,
    val voteCount: Int,
)


data class VoteProfile(
    val id: Int,
    val name: String,
    val introduction: String,
    val profile: ProfileCharacter,
)