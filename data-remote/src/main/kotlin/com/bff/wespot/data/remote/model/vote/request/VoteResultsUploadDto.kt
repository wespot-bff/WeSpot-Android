package com.bff.wespot.data.remote.model.vote.request

import com.bff.wespot.model.vote.request.VoteResultsUpload
import kotlinx.serialization.Serializable

@Serializable
data class VoteResultsUploadDto(
    val votes: List<VoteResultUploadDto>
) {
    fun Result() = VoteResultsUpload(
        voteResults = votes.map { it.toVote() }
    )
}