package com.bff.wespot.model.notification

import com.bff.wespot.model.common.Paging

data class NotificationList(
    override val data: List<Notification>,
    override val hasNext: Boolean,
    override val lastCursorId: Int,
) : Paging<Notification>
