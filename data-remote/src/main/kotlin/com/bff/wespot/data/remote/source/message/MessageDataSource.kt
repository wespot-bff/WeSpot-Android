package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.MessageContentDto
import com.bff.wespot.data.remote.model.message.request.MessageTypeDto
import com.bff.wespot.data.remote.model.message.request.SentMessageDto
import com.bff.wespot.data.remote.model.message.response.MessageIdDto
import com.bff.wespot.data.remote.model.message.response.MessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto

interface MessageDataSource {
    suspend fun getMessageList(messageTypeDto: MessageTypeDto): Result<MessageListDto>

    suspend fun postMessage(sentMessageDto: SentMessageDto): Result<MessageIdDto>

    suspend fun getMessageStatus(): Result<MessageStatusDto>

    suspend fun checkProfanity(content: MessageContentDto): Result<Unit>
}
