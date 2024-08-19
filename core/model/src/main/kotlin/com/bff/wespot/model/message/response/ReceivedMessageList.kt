package com.bff.wespot.model.message.response

import com.bff.wespot.model.common.Paging

data class ReceivedMessageList(
    override val data: List<ReceivedMessage>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<ReceivedMessage>
