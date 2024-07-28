package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.MessageStatus

interface MessageRepository {
    suspend fun getMessageList(messageType: MessageType): Result<MessageList>

    suspend fun postMessage(sentMessage: SentMessage): Result<String>

    suspend fun getMessageStatus(): Result<MessageStatus>

    suspend fun checkProfanity(content: String): Result<Unit>

    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>
}
