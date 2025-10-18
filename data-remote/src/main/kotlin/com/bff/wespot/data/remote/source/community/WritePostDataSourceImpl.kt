package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.PostInfoDto
import com.bff.wespot.data.remote.model.community.chip.CategoryChipsDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class WritePostDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : WritePostDataSource {
    override suspend fun getCategories(): Result<List<CategoryChipsDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/category/details")
            }
        }

    override suspend fun createPost(info: PostInfoDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post")
                setBody(info)
            }
        }

    override suspend fun editPost(postId: String, info: PostInfoDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/post/$postId")
                setBody(info)
            }
        }
}