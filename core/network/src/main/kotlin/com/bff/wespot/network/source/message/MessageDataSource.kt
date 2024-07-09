package com.bff.wespot.network.source.message

import com.bff.wespot.model.message.MessageType
import com.bff.wespot.network.model.result.ErrorDto
import com.bff.wespot.model.result.Result
import com.bff.wespot.network.model.message.MessageListDto

interface MessageDataSource {
    suspend fun getMessageList(messageType: MessageType): Result<MessageListDto, ErrorDto>
}
