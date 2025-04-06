package com.bff.wespot.data.local.model.message

import com.bff.wespot.model.common.Paging

data class ReceivedMessageEntityList(
    override val data: List<ReceivedMessageEntity>,
    override val lastCursorId: Int,
    override val hasNext: Boolean,
) : Paging<ReceivedMessageEntity>
