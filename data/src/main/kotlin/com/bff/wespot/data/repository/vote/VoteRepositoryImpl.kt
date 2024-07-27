package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.mapper.vote.toDto
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.model.vote.request.VoteResultsUpload
import com.bff.wespot.model.vote.response.VoteItems
import com.bff.wespot.model.vote.response.VoteReceived
import com.bff.wespot.model.vote.response.VoteResults
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteDataSource: VoteDataSource
) : VoteRepository {
    override suspend fun getVoteQuestions(): Result<VoteItems> =
        voteDataSource.getVoteQuestions()
            .map {
                it.toVoteItems()
            }

    override suspend fun uploadVoteResults(voteResults: VoteResultsUpload) =
        voteDataSource.uploadVoteResults(voteResults.toDto())
            .isSuccess

    override suspend fun getVoteResults(date: String): Result<VoteResults> =
        voteDataSource.getVoteResults(date)
            .map {
                it.toVoteResults()
            }

    override suspend fun getFirstVoteResults(date: String): Result<VoteResults> =
        voteDataSource.getFirstVoteResults(date)
            .map {
                it.toVoteResults()
            }

    override suspend fun getVoteSent() {

    }

    override suspend fun getVoteReceived(): Result<VoteReceived> =
        voteDataSource.getVoteReceived()
            .map {
                it.toVoteReceived()
            }
}