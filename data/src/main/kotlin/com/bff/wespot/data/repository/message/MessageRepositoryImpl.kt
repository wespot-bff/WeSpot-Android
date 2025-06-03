package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toDto
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.model.message.response.MessageHomeTitle
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.message.response.SenderProfile
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun postMessage(sendMessage: SendMessage): Result<Unit> =
        messageDataSource.postMessage(sendMessage.toDto())

    override suspend fun getSenderProfileList(receiverId: Int): Result<List<SenderProfile>> =
        messageDataSource.getSenderProfileList(receiverId).mapCatching { data ->
            data.map { it.toDomain() }
        }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().mapCatching { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }

    override suspend fun getMessage(messageId: Int): Result<MessageDetail> =
        messageDataSource.getMessage(messageId).mapCatching { messageDto ->
            messageDto.toMessageDetail()
        }

    override suspend fun getMessageHomeTitle(): Result<MessageHomeTitle> =
        messageDataSource.getMessageHomeTitle().mapCatching {
            it.toMessageHomeTitle()
        }
}
