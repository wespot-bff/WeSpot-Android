package com.bff.wespot.data.remote.source.vote

import com.bff.wespot.data.remote.model.vote.request.VoteResultsDto
import com.bff.wespot.data.remote.model.vote.response.VoteItemsDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class VoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
): VoteDataSource {
    override suspend fun getVoteQuestions(): Result<VoteItemsDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/votes/options")
            }
        }

    override suspend fun uploadVoteResults(voteResults: VoteResultsDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/votes")
            }
            setBody(voteResults)
        }
}