package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.MessageId
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.model.result.Result

interface MessageRepository {
    suspend fun getMessageList(messageType: MessageType): Result<MessageList, NetworkError>

    suspend fun postMessage(sentMessage: SentMessage): Result<MessageId, NetworkError>

    suspend fun getMessageStatus(): Result<MessageStatus, NetworkError>
}
