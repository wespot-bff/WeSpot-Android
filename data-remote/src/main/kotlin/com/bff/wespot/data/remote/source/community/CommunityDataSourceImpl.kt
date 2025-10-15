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

    override suspend fun getCommunityContent(
        target: String,
        inquirySize: Int,
        cursorId: Int?
    ): Result<CommunityContentPagingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post")
                cursorId?.let { parameter("cursorId", it) }
                parameter("inquirySize", inquirySize)
                parameter("majorCategoryName", target)
            }
        }

    override suspend fun getCommunitySearchContent(
        keyword: String,
        cursorId: Int?
    ): Result<CommunityContentPagingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post/search")
                parameter("keyword", keyword)
                cursorId?.let { parameter("cursorId", it) }
            }
        }

    override suspend fun getCommunityPostsByType(
        menuType: String,
        cursorId: Int?
    ): Result<CommunityContentPagingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post/${menuType.lowercase()}")
                cursorId?.let { parameter("cursorId", it) }
            }
        }

    override suspend fun onLikeClicked(postId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/${postId}/like")
            }
        }

    override suspend fun onScrapClicked(postId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/${postId}/scrap")
            }
        }
}