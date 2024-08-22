package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toWrittenMessageDto
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.model.message.response.BlockedMessage
import com.bff.wespot.model.message.response.Message
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun postMessage(sentMessage: WrittenMessage): Result<String> {
        return messageDataSource.postMessage(sentMessage.toWrittenMessageDto()).mapCatching {
            it.toString()
        }
    }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().mapCatching { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }

    override suspend fun editMessage(messageId: Int, sentMessage: WrittenMessage): Result<Unit> =
        messageDataSource.editMessage(messageId, sentMessage.toWrittenMessageDto())

    override suspend fun getMessage(messageId: Int): Result<Message> =
        messageDataSource.getMessage(messageId).mapCatching { messageDto ->
            messageDto.toMessage()
        }

    override suspend fun getBlockedMessage(cursorId: Int): Result<List<BlockedMessage>> =
        messageDataSource.getBlockedMessage(cursorId).mapCatching { listDto ->
            listDto.messages.map { it.toBlockedMessage() }
        }
}
