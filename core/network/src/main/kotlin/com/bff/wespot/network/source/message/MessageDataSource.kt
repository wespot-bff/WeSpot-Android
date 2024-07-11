package com.bff.wespot.network.source.message

import com.bff.wespot.network.model.result.ErrorDto
import com.bff.wespot.model.result.Result
import com.bff.wespot.network.model.message.request.MessageTypeDto
import com.bff.wespot.network.model.message.response.MessageIdDto
import com.bff.wespot.network.model.message.response.MessageListDto
import com.bff.wespot.network.model.message.response.MessageStatusDto
import com.bff.wespot.network.model.message.request.SentMessageDto

interface MessageDataSource {
    suspend fun getMessageList(messageTypeDto: MessageTypeDto): Result<MessageListDto, ErrorDto>

    suspend fun postMessage(sentMessageDto: SentMessageDto): Result<MessageIdDto, ErrorDto>

    suspend fun getMessageStatus(): Result<MessageStatusDto, ErrorDto>
}
