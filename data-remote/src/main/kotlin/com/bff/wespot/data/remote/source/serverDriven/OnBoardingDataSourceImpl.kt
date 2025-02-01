package com.bff.wespot.data.remote.source.serverDriven

import com.bff.wespot.data.remote.model.serverDriven.OnBoardingDto
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class OnBoardingDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : OnBoardingDataSource {
    override suspend fun getOnBoarding(category: OnBoardingCategory): Result<OnBoardingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/on-boarding")
                parameter("category", category.name)
            }
        }
}