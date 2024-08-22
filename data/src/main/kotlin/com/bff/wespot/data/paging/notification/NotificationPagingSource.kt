package com.bff.wespot.data.paging.notification

import com.bff.wespot.data.remote.source.notification.NotificationDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.notification.Notification

class NotificationPagingSource(
    private val notificationDataSource: NotificationDataSource,
) : BasePagingSource<Notification, Paging<Notification>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<Notification> {
        val response = notificationDataSource.getNotification(cursorId)
        val data = response.getOrThrow()
        return data.toNotificationList()
    }
}
