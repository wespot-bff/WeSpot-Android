package com.bff.wespot.data.repository.message

import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageRoom
import javax.inject.Inject

class MessageStorageRepositoryImpl @Inject constructor(
    private val messageStorageDataSource: MessageStorageDataSource,
): MessageStorageRepository {
    override suspend fun getMessages(): Result<List<Message>> =
        messageStorageDataSource.getMessageList().mapCatching { messageList ->
            messageList.map { it.toMessage() }
        }

    override suspend fun getBookmarkedMessages(): Result<List<Message>> =
        messageStorageDataSource.getBookmarkedMessageList().mapCatching { messageList ->
            messageList.map { it.toMessage() }
        }

    override suspend fun updateMessageReadStatus(messageId: Int): Result<Unit> =
        messageStorageDataSource.updateMessageReadStatus(messageId = messageId)

    override suspend fun updateMessageBookmarkStatus(messageId: Int): Result<Unit> =
        messageStorageDataSource.updateMessageBookmarkStatus(messageId)

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.deleteMessage(messageId = messageId)

    override suspend fun blockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.blockMessage(messageId = messageId)

    override suspend fun unBlockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.unBlockMessage(messageId = messageId)

    override suspend fun getMessageRoom(roomId: Int): Result<MessageRoom> =
        messageStorageDataSource.getMessageRoom(roomId).mapCatching {
            it.toDomain()
        }
}
