package com.bff.wespot.domain.repository.vote

import com.bff.wespot.model.vote.request.VoteResults
import com.bff.wespot.model.vote.response.VoteItems

interface VoteRepository {
    suspend fun getVoteQuestions(): Result<VoteItems>
    suspend fun uploadVoteResults(voteResults: VoteResults): Boolean
}
