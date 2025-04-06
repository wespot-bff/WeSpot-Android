package com.bff.wespot.data.repository.message

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.bff.wespot.data.local.database.dao.ReceivedMessageDao
import com.bff.wespot.data.local.model.message.ReceivedMessageEntity
import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.ReceivedMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageStorageRepositoryImpl @Inject constructor(
    private val messageStorageDataSource: MessageStorageDataSource,
    private val dao: ReceivedMessageDao,
    private val pager: Pager<Int, ReceivedMessageEntity>,
): MessageStorageRepository {
    override fun fetchReceivedMessageStream(): Flow<PagingData<ReceivedMessage>> =
        pager.flow.map {
            it.map { data ->
                data.toDomain()
            }
        }

    override suspend fun updateMessageReadStatus(messageId: Int): Result<Unit> =
        messageStorageDataSource.updateMessageReadStatus(messageId = messageId)
            .onSuccess {
                dao.updateReadStatus(messageId)
            }

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.deleteMessage(messageId = messageId)
            .onSuccess {
                dao.deleteReceivedMessage(messageId)
            }

    override suspend fun blockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.blockMessage(messageId = messageId)
            .onSuccess {
                dao.deleteReceivedMessage(messageId)
            }

    override suspend fun unBlockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.unBlockMessage(messageId = messageId)

    override suspend fun getReservedMessage(): Result<List<Message>> =
        messageStorageDataSource.getReservedMessage().mapCatching { list ->
            list.messages.map {
                it.toMessage()
            }
        }
}
