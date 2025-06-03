package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.model.message.response.MessageHomeTitle
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.message.response.SenderProfile

interface MessageRepository {
    suspend fun postMessage(sendMessage: SendMessage): Result<Unit>

    suspend fun getSenderProfileList(receiverId: Int): Result<List<SenderProfile>>

    suspend fun getMessageStatus(): Result<MessageStatus>

    suspend fun getMessage(messageId: Int): Result<MessageDetail>

    suspend fun getMessageHomeTitle(): Result<MessageHomeTitle>
}
