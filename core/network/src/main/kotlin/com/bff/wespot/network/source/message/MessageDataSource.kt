package com.bff.wespot.network.source.message

import com.bff.wespot.network.model.message.request.MessageTypeDto
import com.bff.wespot.network.model.message.response.MessageIdDto
import com.bff.wespot.network.model.message.response.MessageListDto
import com.bff.wespot.network.model.message.response.MessageStatusDto
import com.bff.wespot.network.model.message.request.SentMessageDto

interface MessageDataSource {
    suspend fun getMessageList(messageTypeDto: MessageTypeDto): Result<MessageListDto>

    suspend fun postMessage(sentMessageDto: SentMessageDto): Result<MessageIdDto>

    suspend fun getMessageStatus(): Result<MessageStatusDto>
}
