package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.BlockedMessage
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.MessageStatus

interface MessageRepository {
    suspend fun getMessageList(messageType: MessageType, cursorId: Int): Result<MessageList>

    suspend fun postMessage(sentMessage: SentMessage): Result<String>

    suspend fun getMessageStatus(): Result<MessageStatus>

    suspend fun editMessage(messageId: Int, sentMessage: SentMessage): Result<Unit>

    suspend fun getMessage(messageId: Int): Result<Message>

    suspend fun getBlockedMessage(cursorId: Int): Result<List<BlockedMessage>>
}
