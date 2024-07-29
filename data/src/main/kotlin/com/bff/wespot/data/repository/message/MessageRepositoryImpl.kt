package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toMessageTypeDto
import com.bff.wespot.data.mapper.message.toSentMessageDto
import com.bff.wespot.data.remote.model.message.request.MessageContentDto
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.model.message.response.Message
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun getMessageList(
        messageType: MessageType,
    ): Result<MessageList> =
        messageDataSource
            .getMessageList(messageType.toMessageTypeDto())
            .mapCatching { messageListDto ->
                messageListDto.toMessageList()
            }

    override suspend fun postMessage(sentMessage: SentMessage): Result<String> {
        return messageDataSource.postMessage(sentMessage.toSentMessageDto()).mapCatching {
            it.toString()
        }
    }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().mapCatching { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }

    override suspend fun checkProfanity(content: String): Result<Unit> =
        messageDataSource.checkProfanity(MessageContentDto(message = content))

    override suspend fun editMessage(messageId: Int, sentMessage: SentMessage): Result<Unit> =
        messageDataSource.editMessage(messageId, sentMessage.toSentMessageDto())

    override suspend fun getMessage(messageId: Int): Result<Message> =
        messageDataSource.getMessage(messageId).mapCatching { messageDto ->
            messageDto.toMessage()
        }
}
