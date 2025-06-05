package com.bff.wespot.data.paging.message

import androidx.paging.LoadType
import com.bff.wespot.data.local.database.dao.MessageDao
import com.bff.wespot.data.local.model.message.MessageEntity
import com.bff.wespot.data.mapper.message.toEntity
import com.bff.wespot.data.paging.base.BaseRemoteMediator
import com.bff.wespot.data.remote.model.message.response.MessageListDto
import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import com.bff.wespot.model.common.Paging

class MessageRemoteMediator(
    private val source: MessageStorageDataSource,
    private val dao: MessageDao,
) : BaseRemoteMediator<MessageEntity, Paging<MessageEntity>>() {
    override suspend fun fetchItems(loadCursorId: Int?): Paging<MessageEntity> {
        // TODO 쪽지 방 페이징 적용시 사용
        /*val response = source.getMessageList(loadCursorId)
        val data = response.getOrThrow()*/
        val data = MessageListDto(listOf(), 0, false)
        val lastCursorId = when {
            data.hasNext -> data.lastCursorId
            else -> null
        }
        return data.toEntity(lastCursorId = lastCursorId)
    }

    override suspend fun saveItems(loadType: LoadType, items: List<MessageEntity>) {
        if (loadType == LoadType.REFRESH) {
            dao.clearMessages()
        }
        dao.upsertAll(items)
    }

    override fun getNextCursorId(item: MessageEntity): Int? = item.lastCursorId
}
