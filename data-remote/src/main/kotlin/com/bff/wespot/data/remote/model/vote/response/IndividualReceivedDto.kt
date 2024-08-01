package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.ReceivedResult
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
    val user: VoteUserDto,
    val rate: Int,
    val voteCount: Int,
) {
    fun toReceivedResult() = ReceivedResult(
        voteOption = voteOption.toVoteOption(),
        user = user.toVoteUser(),
        rate = rate,
        voteCount = voteCount,
    )
}
