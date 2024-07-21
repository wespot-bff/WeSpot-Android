package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.MessageTypeDto
import com.bff.wespot.data.remote.model.message.request.SentMessageDto
import com.bff.wespot.data.remote.model.message.request.type
import com.bff.wespot.data.remote.model.message.response.MessageIdDto
import com.bff.wespot.data.remote.model.message.response.MessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.*
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
): MessageDataSource {
    override suspend fun getMessageList(
        messageTypeDto: MessageTypeDto,
    ): Result<MessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("messages")
                parameter("type", messageTypeDto.type())
            }
        }

    override suspend fun postMessage(
        sentMessageDto: SentMessageDto,
    ): Result<MessageIdDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("messages/send")
                setBody(sentMessageDto)
            }
        }

    override suspend fun getMessageStatus(): Result<MessageStatusDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("messages/status/me")
            }
        }
    }