package com.bff.wespot.data.repository.message

import com.bff.wespot.data.paging.message.MessageReceivedPagingSource
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.ReceivedMessage
import javax.inject.Inject

class MessageReceivedPagingRepository @Inject constructor(
    private val dataSource: MessageDataSource,
) : BasePagingRepository<ReceivedMessage, Paging<ReceivedMessage>>() {
    override fun pagingSource(
        parameter: Map<String, String>?,
    ): BasePagingSource<ReceivedMessage, Paging<ReceivedMessage>> =
        MessageReceivedPagingSource(dataSource)
}
