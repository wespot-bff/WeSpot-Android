package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.MessageList
import com.bff.wespot.model.message.MessageType
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.model.result.Result

interface MessageRepository {
    suspend fun getMessageList(messageType: MessageType): Result<MessageList, NetworkError>
}
