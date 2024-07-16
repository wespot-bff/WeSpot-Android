package com.bff.wespot.network.source.auth

import com.bff.wespot.network.extensions.safeRequest
import com.bff.wespot.network.model.auth.SchoolListDto
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : AuthDataSource {
    override suspend fun getSchoolList(search: String): Result<SchoolListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/auth/signup/schools/search")
                parameter("name", search)
            }
        }
}