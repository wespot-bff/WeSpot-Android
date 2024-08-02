package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.SentVoteResult
import com.bff.wespot.model.vote.response.SentVoteStorage
import kotlinx.serialization.Serializable

@Serializable
data class SentVoteStorageDto (
    val vote: SentVoteResultDto,
) {
    fun toSentVoteStorage(): SentVoteStorage {
        return SentVoteStorage(
            vote = vote.toSentVoteResult()
        )
    }
}

@Serializable
data class SentVoteResultDto(
    val voteOption: VoteOptionDto,
    val user: VoteProfileDto,
) {
    fun toSentVoteResult(): SentVoteResult {
        return SentVoteResult(
            voteOption = voteOption.toVoteOption(),
            voteCount = 0,
            user = user.toProfile()
        )
    }
}