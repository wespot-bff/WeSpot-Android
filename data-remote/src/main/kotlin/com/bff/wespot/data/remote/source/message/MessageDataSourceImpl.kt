package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.MessageTypeDto
import com.bff.wespot.data.remote.model.message.request.SentMessageDto
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
    override suspend fun getMessageList(
        messageTypeDto: MessageTypeDto,
        cursorId: Int,
    ): Result<MessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages")
                parameter("type", messageTypeDto.name)
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun postMessage(
        sentMessageDto: SentMessageDto,
    ): Result<MessageIdDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/messages/send")
                setBody(sentMessageDto)
            }
        }

    override suspend fun getMessageStatus(): Result<MessageStatusDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/status/me")
            }
        }

    override suspend fun editMessage(messageId: Int, sentMessageDto: SentMessageDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/messages/$messageId")
                setBody(sentMessageDto)
            }
        }

    override suspend fun getMessage(messageId: Int): Result<MessageDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/$messageId")
            }
        }

    override suspend fun getBlockedMessage(cursorId: Int): Result<BlockedMessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/blocked")
                parameter("cursorId", cursorId)
            }
        }
}
