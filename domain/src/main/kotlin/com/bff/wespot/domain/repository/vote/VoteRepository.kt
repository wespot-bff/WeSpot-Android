package com.bff.wespot.domain.repository.vote

import com.bff.wespot.model.vote.request.VoteResultsUpload
import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.IndividualSent
import com.bff.wespot.model.vote.response.VoteItems
import com.bff.wespot.model.vote.response.VoteResults

interface VoteRepository {
    suspend fun getVoteQuestions(): Result<VoteItems>
    suspend fun uploadVoteResults(voteResults: VoteResultsUpload): Boolean
    suspend fun getVoteResults(date: String): Result<VoteResults>
    suspend fun getFirstVoteResults(date: String): Result<VoteResults>
    suspend fun getReceivedVote(
        date: String,
        optionId: Int,
    ): Result<IndividualReceived>

    suspend fun getSentVote(date: String, optionId: Int): Result<IndividualSent>
}
