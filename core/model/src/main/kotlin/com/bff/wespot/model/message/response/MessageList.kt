package com.bff.wespot.model.message.response

import com.bff.wespot.model.common.Paging

data class MessageList(
    override val data: List<Message>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<Message>
