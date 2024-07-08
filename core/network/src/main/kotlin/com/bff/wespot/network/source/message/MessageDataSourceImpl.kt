package com.bff.wespot.network.source.message

import com.bff.wespot.model.message.MessageType
import com.bff.wespot.model.message.name
import com.bff.wespot.network.model.message.MessageListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val client: HttpClient,
): MessageDataSource {
    override suspend fun getMessageList(
        messageType: MessageType,
    ): MessageListDto {
        val response = client.get("messages") {
            parameter("type", messageType.name())
        }
        return response.body<MessageListDto>()
    }
}