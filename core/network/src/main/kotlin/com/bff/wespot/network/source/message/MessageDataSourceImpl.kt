package com.bff.wespot.network.source.message

import com.bff.wespot.model.message.MessageType
import com.bff.wespot.model.message.name
import com.bff.wespot.network.model.result.ErrorDto
import com.bff.wespot.model.result.Result
import com.bff.wespot.network.model.result.parseResponse
import com.bff.wespot.network.model.message.MessageListDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val client: HttpClient,
): MessageDataSource {
    override suspend fun getMessageList(
        messageType: MessageType,
    ): Result<MessageListDto, ErrorDto> {
        return parseResponse {
            client.get("messages") {
                parameter("type", messageType.name())
            }
        }
    }
}