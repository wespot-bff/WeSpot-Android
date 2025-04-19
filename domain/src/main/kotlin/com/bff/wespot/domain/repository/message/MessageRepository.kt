package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageStatus

interface MessageRepository {
    suspend fun postMessage(sendMessage: SendMessage): Result<Unit>

    suspend fun getMessageStatus(): Result<MessageStatus>

    suspend fun getMessage(messageId: Int): Result<Message>
}
