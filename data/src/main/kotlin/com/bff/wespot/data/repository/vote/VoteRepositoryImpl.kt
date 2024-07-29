package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.mapper.vote.toDto
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.model.vote.request.VoteResultsUpload
import com.bff.wespot.model.vote.response.VoteItems
import com.bff.wespot.model.vote.response.VoteReceived
import com.bff.wespot.model.vote.response.VoteResults
import com.bff.wespot.model.vote.response.VoteSent
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteDataSource: VoteDataSource
) : VoteRepository {
    override suspend fun getVoteQuestions(): Result<VoteItems> =
        voteDataSource.getVoteQuestions()
            .mapCatching {
                it.toVoteItems()
            }

    override suspend fun uploadVoteResults(voteResults: VoteResultsUpload) =
        voteDataSource.uploadVoteResults(voteResults.toDto())
            .isSuccess

    override suspend fun getVoteResults(date: String): Result<VoteResults> =
        voteDataSource.getVoteResults(date)
            .mapCatching {
                it.toVoteResults()
            }

    override suspend fun getFirstVoteResults(date: String): Result<VoteResults> =
        voteDataSource.getFirstVoteResults(date)
            .mapCatching {
                it.toVoteResults()
            }

    override suspend fun getVoteSent(): Result<VoteSent> =
        voteDataSource.getVoteSent()
            .mapCatching {
                it.toVoteSent()
            }

    override suspend fun getVoteReceived(): Result<VoteReceived> =
        voteDataSource.getVoteReceived()
            .mapCatching {
                it.toVoteReceived()
            }
}