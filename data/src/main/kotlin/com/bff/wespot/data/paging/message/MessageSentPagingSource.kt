package com.bff.wespot.data.paging.message

import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.SentMessage

class MessageSentPagingSource(
    private val messageDataSource: MessageDataSource,
) : BasePagingSource<SentMessage, Paging<SentMessage>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<SentMessage> {
        val response = messageDataSource.getSentMessageList(cursorId)
        val data = response.getOrThrow()
        return data.toSentMessageList()
    }
}
