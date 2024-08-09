package com.bff.wespot.data.repository.notification

import com.bff.wespot.data.remote.source.notification.NotificationDataSource
import com.bff.wespot.domain.repository.notification.NotificationRepository
import com.bff.wespot.model.notification.Notification
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
): NotificationRepository {
    override suspend fun getNotificationList(): Result<List<Notification>> =
        notificationDataSource.getNotification().mapCatching {
            it.notifications.map { notificationDto ->
                notificationDto.toNotification()
            }
        }

    override suspend fun updateNotificationReadStatus(id: Int): Result<Unit> =
        notificationDataSource.updateNotificationReadStatus(id)
}
