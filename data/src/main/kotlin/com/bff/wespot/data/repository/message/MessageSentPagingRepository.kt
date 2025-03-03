package com.bff.wespot.data.repository.message

import com.bff.wespot.data.paging.message.MessageSentPagingSource
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.SentMessage
import javax.inject.Inject

class MessageSentPagingRepository @Inject constructor(
    private val dataSource: MessageDataSource,
) : BasePagingRepository<SentMessage, Paging<SentMessage>>() {
    override fun pagingSource(
        parameter: Map<String, String>?,
    ): BasePagingSource<SentMessage, Paging<SentMessage>> = MessageSentPagingSource(dataSource)
}
