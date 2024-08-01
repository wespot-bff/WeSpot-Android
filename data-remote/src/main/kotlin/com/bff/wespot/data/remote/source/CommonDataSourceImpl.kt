package com.bff.wespot.data.remote.source

import com.bff.wespot.data.remote.model.ProfanityDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class CommonDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : CommonDataSource {
    override suspend fun checkProfanity(content: ProfanityDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("check-profanity")
                setBody(content)
            }
        }
}