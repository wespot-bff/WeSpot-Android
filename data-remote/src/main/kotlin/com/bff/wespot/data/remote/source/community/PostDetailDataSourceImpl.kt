package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.PostCommentDto
import com.bff.wespot.data.remote.model.community.PostDetailDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class PostDetailDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : PostDetailDataSource {
    override suspend fun getPostDetail(postId: String): Result<PostDetailDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post/$postId")
            }
        }

    override suspend fun getPostComments(postId: String): Result<List<PostCommentDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post/comment")
                parameter("postId", postId)
            }
        }
}