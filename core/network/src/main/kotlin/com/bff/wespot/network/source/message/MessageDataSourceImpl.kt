package com.bff.wespot.network.source.message

import com.bff.wespot.network.model.result.ErrorDto
import com.bff.wespot.model.result.Result
import com.bff.wespot.network.model.message.request.MessageTypeDto
import com.bff.wespot.network.model.message.request.SentMessageDto
import com.bff.wespot.network.model.message.request.name
import com.bff.wespot.network.model.message.response.MessageIdDto
import com.bff.wespot.network.model.result.parseResponse
import com.bff.wespot.network.model.message.response.MessageListDto
import com.bff.wespot.network.model.message.response.MessageStatusDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val client: HttpClient,
): MessageDataSource {
    override suspend fun getMessageList(
        messageTypeDto: MessageTypeDto,
    ): Result<MessageListDto, ErrorDto> {
        return parseResponse {
            client.get("messages") {
                parameter("type", messageTypeDto.name())
            }
        }
    }

    override suspend fun postMessage(
        sentMessageDto: SentMessageDto,
    ): Result<MessageIdDto, ErrorDto> {
        return parseResponse {
            client.post("messages/send") {
                setBody(sentMessageDto)
            }
        }
    }

    override suspend fun getMessageStatus(): Result<MessageStatusDto, ErrorDto> {
        return parseResponse {
            client.get("messages/status/me")
        }
    }
}