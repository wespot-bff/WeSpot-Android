package com.bff.wespot.data.local.model.message

import com.bff.wespot.model.common.Paging

data class MessageEntityList(
    override val data: List<MessageEntity>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<MessageEntity>
