package com.bff.wespot.model.message.response

import com.bff.wespot.model.common.Paging

data class BlockedMessageList(
    override val data: List<BlockedMessage>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<BlockedMessage>
