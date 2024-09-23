package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.WrittenMessageDto
import com.bff.wespot.data.remote.model.message.response.BlockedMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.data.remote.model.message.response.MessageIdDto
import com.bff.wespot.data.remote.model.message.response.MessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
): MessageDataSource {
    override suspend fun getReceivedMessageList(cursorId: Int?): Result<MessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages")
                parameter("type", "RECEIVED")
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun getSentMessageList(cursorId: Int?): Result<MessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages")
                parameter("type", "SENT")
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun postMessage(
        writtenMessageDto: WrittenMessageDto,
    ): Result<MessageIdDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/messages/send")
                setBody(writtenMessageDto)
            }
        }

    override suspend fun getMessageStatus(): Result<MessageStatusDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/status/me")
            }
        }

    override suspend fun editMessage(messageId: Int, writtenMessageDto: WrittenMessageDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/messages/$messageId")
                setBody(writtenMessageDto)
            }
        }

    override suspend fun getMessage(messageId: Int): Result<MessageDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/$messageId")
            }
        }

    override suspend fun getBlockedMessage(cursorId: Int?): Result<BlockedMessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/blocked")
                parameter("cursorId", cursorId)
            }
        }
}
