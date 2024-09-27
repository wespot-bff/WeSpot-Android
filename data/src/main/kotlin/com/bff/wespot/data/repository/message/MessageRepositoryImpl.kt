package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toWrittenMessageDto
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.model.message.response.Message
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun postMessage(writtenMessage: WrittenMessage): Result<String> {
        return messageDataSource.postMessage(writtenMessage.toWrittenMessageDto()).mapCatching {
            it.toString()
        }
    }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().mapCatching { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }

    override suspend fun editMessage(messageId: Int, writtenMessage: WrittenMessage): Result<Unit> =
        messageDataSource.editMessage(messageId, writtenMessage.toWrittenMessageDto())

    override suspend fun getMessage(messageId: Int): Result<Message> =
        messageDataSource.getMessage(messageId).mapCatching { messageDto ->
            messageDto.toMessage()
        }
}
