package com.bff.wespot.data.remote.source.vote

import com.bff.wespot.data.remote.model.vote.request.VoteResultsUploadDto
import com.bff.wespot.data.remote.model.vote.response.VoteItemsDto
import com.bff.wespot.data.remote.model.vote.response.VoteReceivedDto
import com.bff.wespot.data.remote.model.vote.response.VoteResultsDto
import com.bff.wespot.data.remote.model.vote.response.VoteSentDto

interface VoteDataSource {
    suspend fun getVoteQuestions(): Result<VoteItemsDto>
    suspend fun uploadVoteResults(voteResults: VoteResultsUploadDto): Result<Unit>
    suspend fun getVoteResults(date: String): Result<VoteResultsDto>
    suspend fun getFirstVoteResults(date: String): Result<VoteResultsDto>
    suspend fun getVoteSent(): Result<VoteSentDto>
    suspend fun getVoteReceived(): Result<VoteReceivedDto>
}