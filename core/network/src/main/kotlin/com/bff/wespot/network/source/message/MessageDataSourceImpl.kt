package com.bff.wespot.network.source.message

import com.bff.wespot.network.model.result.ErrorDto
import com.bff.wespot.model.result.Result
import com.bff.wespot.network.extensions.safeRequest
import com.bff.wespot.network.model.message.request.MessageTypeDto
import com.bff.wespot.network.model.message.request.SentMessageDto
import com.bff.wespot.network.model.message.request.name
import com.bff.wespot.network.model.message.response.MessageIdDto
import com.bff.wespot.network.model.message.response.MessageListDto
import com.bff.wespot.network.model.message.response.MessageStatusDto
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
    ): Result<MessageListDto, ErrorDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("messages")
                parameter("type", messageTypeDto.name())
            }
        }

    override suspend fun postMessage(
        sentMessageDto: SentMessageDto,
    ): Result<MessageIdDto, ErrorDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("messages/send")
                setBody(sentMessageDto)
            }
        }

    override suspend fun getMessageStatus(): Result<MessageStatusDto, ErrorDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("messages/status/me")
            }
        }
    }
