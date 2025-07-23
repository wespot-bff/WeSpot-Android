package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.chip.CategoryChipsDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
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
}