package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.mapper.vote.toDto
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.model.vote.request.VoteResults
import com.bff.wespot.model.vote.response.VoteItems
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteDataSource: VoteDataSource
) : VoteRepository {
    override suspend fun getVoteQuestions(): Result<VoteItems> =
        voteDataSource.getVoteQuestions()
            .map {
                it.toVoteItems()
            }

    override suspend fun uploadVoteResults(voteResults: VoteResults) =
        voteDataSource.uploadVoteResults(voteResults.toDto())
            .isSuccess
}