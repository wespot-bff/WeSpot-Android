package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.model.message.response.MessageHomeTitle
import com.bff.wespot.model.message.response.MessageStatus

interface MessageRepository {
    suspend fun postMessage(writtenMessage: WrittenMessage): Result<String>

    suspend fun getMessageStatus(): Result<MessageStatus>

    suspend fun editMessage(messageId: Int, writtenMessage: WrittenMessage): Result<Unit>

    suspend fun getMessage(messageId: Int): Result<MessageDetail>

    suspend fun getMessageHomeTitle(): Result<MessageHomeTitle>
}
