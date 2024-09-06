package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.response.ReservedMessageListDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class MessageStorageDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : MessageStorageDataSource {
    override suspend fun updateMessageReadStatus(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/messages/$messageId/read")
            }
        }

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Delete
                path("api/v1/messages/$messageId")
            }
        }

    override suspend fun blockMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/messages/$messageId/block")
            }
        }

    override suspend fun unBlockMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/messages/$messageId/unblock")
            }
        }

    override suspend fun getReservedMessage(): Result<ReservedMessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/scheduled")
            }
        }
}
