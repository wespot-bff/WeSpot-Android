package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.data.remote.model.user.response.ProfileCharacterDto
import com.bff.wespot.model.vote.response.Result
import com.bff.wespot.model.vote.response.VoteProfile
import com.bff.wespot.model.vote.response.VoteResult
import com.bff.wespot.model.vote.response.VoteResults
import kotlinx.serialization.Serializable

@Serializable
data class VoteResultsDto(
    val voteResults: List<VoteResultDto>
) {
    fun toVoteResults() = VoteResults(
        voteResults = voteResults.map { it.toVoteResult() }
    )
}

@Serializable
data class VoteResultDto(
    val voteOption: VoteOptionDto,
    val results: List<ResultDto>,
) {
    fun toVoteResult() = VoteResult(
        voteOption = voteOption.toVoteOption(),
        results = results.map { it.toResult() },
    )
}

@Serializable
data class ResultDto(
    val user: VoteProfileDto,
    val voteCount: Int,
) {
    fun toResult() = Result(
        user = user.toProfile(),
        voteCount = voteCount,
    )
}

@Serializable
data class VoteProfileDto(
    val id: Int,
    val name: String,
    val introduction: String,
    val profile: ProfileCharacterDto
) {
    fun toProfile() = VoteProfile(
        id = id,
        name = name,
        introduction = introduction,
        profile = profile.toProfileCharacter(),
    )
}