package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.MessageUsageDto
import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class MessageSettingDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : MessageSettingDataSource {
    override suspend fun getBlockedMessageList(): Result<List<MessageDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v2/messages/blocked")
            }
        }

    override suspend fun updateMessageBlockStatus(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Patch
                path("api/v2/messages/$messageId/block")
            }
        }

    override suspend fun updateMessageUsageStatus(messageUsage: MessageUsageDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Patch
                path("/api/v2/messages/setting")
            }
            setBody(messageUsage)
        }
}
