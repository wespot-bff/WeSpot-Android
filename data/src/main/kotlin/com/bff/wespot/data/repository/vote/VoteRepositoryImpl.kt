package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.mapper.vote.toDto
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.model.vote.request.VoteResultsUpload
import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.IndividualSent
import com.bff.wespot.model.vote.response.VoteItems
import com.bff.wespot.model.vote.response.VoteResults
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

    override suspend fun getReceivedVote(
        date: String,
        optionId: Int,
    ): Result<IndividualReceived> =
        voteDataSource.getReceivedVote(date, optionId)
            .mapCatching {
                it.toIndividualReceived()
            }

    override suspend fun getSentVote(date: String, optionId: Int): Result<IndividualSent> =
        voteDataSource.getSentVote(date, optionId)
            .mapCatching {
                it.toIndividualSent()
            }
}