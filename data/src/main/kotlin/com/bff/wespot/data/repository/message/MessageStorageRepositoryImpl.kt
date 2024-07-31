package com.bff.wespot.data.repository.message

import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.model.message.response.Message
import javax.inject.Inject

class MessageStorageRepositoryImpl @Inject constructor(
    private val messageStorageDataSource: MessageStorageDataSource,
): MessageStorageRepository {
    override suspend fun updateMessageReadStatus(messageId: Int): Result<Unit> =
        messageStorageDataSource.updateMessageReadStatus(messageId = messageId)

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.deleteMessage(messageId = messageId)

    override suspend fun blockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.blockMessage(messageId = messageId)

    override suspend fun reportMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.reportMessage(messageId = messageId)

    override suspend fun getReservedMessage(): Result<List<Message>> =
        messageStorageDataSource.getReservedMessage().mapCatching { list ->
            list.messages.map {
                it.toMessage()
            }
        }
}
