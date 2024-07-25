package com.bff.wespot.data.remote.source.vote

import com.bff.wespot.data.remote.model.vote.request.VoteResultsDto
import com.bff.wespot.data.remote.model.vote.response.VoteItemsDto

interface VoteDataSource {
    suspend fun getVoteQuestions(): Result<VoteItemsDto>
    suspend fun uploadVoteResults(voteResults: VoteResultsDto): Result<Unit>
}