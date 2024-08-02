package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.data.remote.model.user.response.ProfileCharacterDto
import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.ReceivedResult
import com.bff.wespot.model.vote.response.ReceivedUser
import kotlinx.serialization.Serializable

@Serializable
data class IndividualReceivedDto(
    val voteResult: ReceivedResultDto,
) {
    fun toIndividualReceived() = IndividualReceived(
        voteResult = voteResult.toReceivedResult()
    )
}

@Serializable
data class ReceivedResultDto(
    val voteOption: VoteOptionDto,
    val user: ReceivedUserDto,
    val rate: Int,
    val voteCount: Int,
) {
    fun toReceivedResult() = ReceivedResult(
        voteOption = voteOption.toVoteOption(),
        user = user.toReceivedUser(),
        rate = rate,
        voteCount = voteCount,
    )
}

@Serializable
data class ReceivedUserDto(
    val id: Int,
    val name: String,
    val introduction: String,
    val profile: ProfileCharacterDto,
) {
    fun toReceivedUser() = ReceivedUser(
        id = id,
        name = name,
        introduction = introduction,
        profile = profile.toProfileCharacter(),
    )
}