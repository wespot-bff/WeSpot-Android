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
                path("messages/$messageId/read")
            }
        }

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Delete
                path("messages/$messageId")
            }
        }

    override suspend fun blockMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("messages/$messageId/block")
            }
        }

    override suspend fun reportMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("messages/$messageId/report")
            }
        }

    override suspend fun getReservedMessage(): Result<ReservedMessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("messages/scheduled")
            }
        }
}
