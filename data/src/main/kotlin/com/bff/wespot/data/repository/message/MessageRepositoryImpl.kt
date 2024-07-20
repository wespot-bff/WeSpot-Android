package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toMessageTypeDto
import com.bff.wespot.data.mapper.message.toSentMessageDto
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.data.remote.source.message.MessageDataSource
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun getMessageList(
        messageType: MessageType,
    ): Result<MessageList> =
        messageDataSource
            .getMessageList(messageType.toMessageTypeDto())
            .map { messageListDto ->
                messageListDto.toMessageList()
            }

    override suspend fun postMessage(sentMessage: SentMessage): Result<String> {
        return messageDataSource.postMessage(sentMessage.toSentMessageDto()).map { it.toString() }
    }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().map { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }
}
