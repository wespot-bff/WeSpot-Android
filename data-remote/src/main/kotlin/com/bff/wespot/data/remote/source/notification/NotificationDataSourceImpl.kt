package com.bff.wespot.data.remote.source.notification

import com.bff.wespot.data.remote.model.notification.NotificationListDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
): NotificationDataSource {
    override suspend fun getNotification(cursorId: Int?): Result<NotificationListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/notifications")
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun updateNotificationReadStatus(id: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Patch
                path("api/v1/notifications/$id")
            }
        }
}
