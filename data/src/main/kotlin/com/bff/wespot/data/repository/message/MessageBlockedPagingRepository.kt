package com.bff.wespot.data.repository.message

import com.bff.wespot.data.paging.message.MessageBlockedPagingSource
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.BlockedMessage
import javax.inject.Inject

class MessageBlockedPagingRepository @Inject constructor(
    private val dataSource: MessageDataSource,
) : BasePagingRepository<BlockedMessage, Paging<BlockedMessage>>() {
    override fun pagingSource(
        parameter: Map<String, String>?,
    ): BasePagingSource<BlockedMessage, Paging<BlockedMessage>> =
        MessageBlockedPagingSource(dataSource)
}
