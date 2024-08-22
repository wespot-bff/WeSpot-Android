package com.bff.wespot.model.message.response

import com.bff.wespot.model.common.Paging

data class SentMessageList(
    override val data: List<SentMessage>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<SentMessage>
