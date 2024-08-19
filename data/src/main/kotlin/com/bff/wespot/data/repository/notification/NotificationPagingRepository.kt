package com.bff.wespot.data.repository.notification

import com.bff.wespot.data.paging.notification.NotificationPagingSource
import com.bff.wespot.data.remote.source.notification.NotificationDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.notification.Notification
import javax.inject.Inject

class NotificationPagingRepository @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : BasePagingRepository<Notification, Paging<Notification>>() {
    override fun pagingSource(
        parameter: Map<String, String>?,
    ): BasePagingSource<Notification, Paging<Notification>> =
        NotificationPagingSource(notificationDataSource)
}
