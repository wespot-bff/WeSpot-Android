package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class MessageStorageDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : MessageStorageDataSource {
    override suspend fun getMessageList(): Result<List<MessageDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v2/messages")
            }
        }

    override suspend fun getBookmarkedMessageList(): Result<List<MessageDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v2/messages/bookmarks")
            }
        }

    override suspend fun updateMessageReadStatus(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/messages/$messageId/read")
            }
        }

    override suspend fun updateMessageBookmarkStatus(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            method = HttpMethod.Patch
            url {
                path("api/v2/messages/$messageId/bookmark")
            }
        }

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Delete
                path("api/v1/messages/$messageId")
            }
        }
}
