package com.bff.wespot.data.repository.message

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.bff.wespot.data.di.BookmarkedMessagesPager
import com.bff.wespot.data.di.MessagesPager
import com.bff.wespot.data.local.database.dao.MessageDao
import com.bff.wespot.data.local.model.message.MessageEntity
import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.model.message.response.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageStorageRepositoryImpl @Inject constructor(
    private val messageStorageDataSource: MessageStorageDataSource,
    private val dao: MessageDao,
    @MessagesPager private val messagesPager: Pager<Int, MessageEntity>,
    @BookmarkedMessagesPager private val bookmarkedMessagesPager: Pager<Int, MessageEntity>,
): MessageStorageRepository {
    override fun fetchMessagesStream(): Flow<PagingData<Message>> =
        messagesPager.flow.map {
            it.map { data ->
                data.toDomain()
            }
        }

    override fun fetchBookmarkedMessagesStream(): Flow<PagingData<Message>> =
        bookmarkedMessagesPager.flow.map {
            it.map { data ->
                data.toDomain()
            }
        }

    override suspend fun updateMessageReadStatus(messageId: Int): Result<Unit> =
        messageStorageDataSource.updateMessageReadStatus(messageId = messageId)
            .onSuccess {
                dao.updateReadStatus(messageId)
            }

    override suspend fun updateMessageBookmarkStatus(
        messageId: Int
    ): Result<Unit> =
        messageStorageDataSource.updateMessageBookmarkStatus(messageId)
            .onSuccess {
                dao.updateBookmarkStatus(messageId)
            }

    override suspend fun deleteMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.deleteMessage(messageId = messageId)
            .onSuccess {
                dao.deleteMessage(messageId)
            }

    override suspend fun blockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.blockMessage(messageId = messageId)
            .onSuccess {
                dao.deleteMessage(messageId)
            }

    override suspend fun unBlockMessage(messageId: Int): Result<Unit> =
        messageStorageDataSource.unBlockMessage(messageId = messageId)
}
