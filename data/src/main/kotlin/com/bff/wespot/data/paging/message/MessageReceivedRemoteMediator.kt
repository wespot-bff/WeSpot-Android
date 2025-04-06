package com.bff.wespot.data.paging.message

import androidx.paging.LoadType
import com.bff.wespot.data.local.database.dao.ReceivedMessageDao
import com.bff.wespot.data.local.model.message.ReceivedMessageEntity
import com.bff.wespot.data.mapper.message.toEntity
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.data.paging.base.BaseRemoteMediator
import com.bff.wespot.model.common.Paging

class MessageReceivedRemoteMediator(
    private val source: MessageDataSource,
    private val dao: ReceivedMessageDao,
) : BaseRemoteMediator<ReceivedMessageEntity, Paging<ReceivedMessageEntity>>() {
    override suspend fun fetchItems(loadCursorId: Int?): Paging<ReceivedMessageEntity> {
        val response = source.getReceivedMessageList(loadCursorId)
        val data = response.getOrThrow()
        val lastCursorId = when {
            data.hasNext -> data.lastCursorId
            else -> null
        }
        return data.toEntity(lastCursorId = lastCursorId)
    }

    override suspend fun saveItems(loadType: LoadType, items: List<ReceivedMessageEntity>) {
        if (loadType == LoadType.REFRESH) {
            dao.clearReceivedMessage()
        }
        dao.upsertAll(items)
    }

    override fun getNextCursorId(item: ReceivedMessageEntity): Int? = item.lastCursorId
}
