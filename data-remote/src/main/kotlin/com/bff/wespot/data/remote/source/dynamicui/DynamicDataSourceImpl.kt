package com.bff.wespot.data.remote.source.dynamicui

import com.bff.wespot.data.remote.model.dynamicui.FeatureOverviewDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class DynamicDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
): DynamicUiDataSource {
    override suspend fun getFeatureOverview(notificationType: String): Result<FeatureOverviewDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/update-modal")
                parameter("publishNotificationType", notificationType)
            }
        }
}
