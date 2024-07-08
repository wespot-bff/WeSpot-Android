package com.bff.wespot.network.source.message

import com.bff.wespot.model.message.MessageType
import com.bff.wespot.network.model.message.MessageListDto

interface MessageDataSource {
    suspend fun getMessageList(messageType: MessageType): MessageListDto
}
