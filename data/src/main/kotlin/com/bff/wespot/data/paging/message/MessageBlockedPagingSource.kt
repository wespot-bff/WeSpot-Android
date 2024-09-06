package com.bff.wespot.data.paging.message

import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.BlockedMessage

class MessageBlockedPagingSource (
    private val messageDataSource: MessageDataSource,
) : BasePagingSource<BlockedMessage, Paging<BlockedMessage>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<BlockedMessage> {
        val response = messageDataSource.getBlockedMessage(cursorId)
        val data = response.getOrThrow()
        return data.toBlockedMessageList()
    }
}
