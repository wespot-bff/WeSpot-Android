package com.bff.wespot.data.paging.message

import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.ReceivedMessage

class MessageReceivedPagingSource(
    private val messageDataSource: MessageDataSource,
) : BasePagingSource<ReceivedMessage, Paging<ReceivedMessage>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<ReceivedMessage> {
        val response = messageDataSource.getReceivedMessageList(cursorId)
        val data = response.getOrThrow()
        return data.toReceivedMessageList()
    }
}
