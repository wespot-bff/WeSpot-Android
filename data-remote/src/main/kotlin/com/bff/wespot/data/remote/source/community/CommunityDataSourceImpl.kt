package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.CommunityContentPagingDto
import com.bff.wespot.data.remote.model.community.chip.BaseChipDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class CommunityDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : CommunityDataSource {
    override suspend fun getCommunityChips(): Result<List<BaseChipDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/category")
            }
        }

    override suspend fun getCommunityContent(cursorId: Int?): Result<CommunityContentPagingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/community/content")
                cursorId?.let { parameter("cursorId", it) }
            }
        }
}