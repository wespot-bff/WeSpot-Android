package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.data.remote.model.user.response.ProfileCharacterDto
import com.bff.wespot.model.vote.response.IndividualSent
import com.bff.wespot.model.vote.response.SentResult
import com.bff.wespot.model.vote.response.SentUser
import kotlinx.serialization.Serializable

@Serializable
data class IndividualSentDto(
    val voteResult: SentResultDto
) {
    fun toIndividualSent() = IndividualSent (
        voteResult = voteResult.toSentResult()
    )
}

@Serializable
data class SentResultDto(
    val voteOption: VoteOptionDto,
    val voteUsers: List<SentUsersDto>
) {
    fun toSentResult() = SentResult (
        voteOption = voteOption.toVoteOption(),
        voteUsers = voteUsers.map { it.toSentUser() }
    )
}

@Serializable
data class SentUsersDto(
    val user: SentUserDto,
) {
    fun toSentUser() = SentUser (
        id = user.id,
        name = user.name,
        profile = user.profile.toProfileCharacter(),
    )
}

@Serializable
data class SentUserDto(
    val id: Int,
    val name: String,
    val profile: ProfileCharacterDto
)