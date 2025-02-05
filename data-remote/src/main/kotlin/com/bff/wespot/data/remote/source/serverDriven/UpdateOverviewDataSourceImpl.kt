package com.bff.wespot.data.remote.source.serverDriven

import com.bff.wespot.data.remote.model.serverDriven.overview.UpdateOverviewDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class UpdateOverviewDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
): UpdateOverviewDataSource {
    override suspend fun getUpdateOverview(notificationType: String): Result<UpdateOverviewDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/update-modal")
                parameter("publishNotificationType", notificationType)
            }
        }
}
