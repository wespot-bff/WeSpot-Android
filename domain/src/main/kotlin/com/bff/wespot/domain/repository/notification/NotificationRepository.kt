package com.bff.wespot.domain.repository.notification

import com.bff.wespot.model.notification.Notification

interface NotificationRepository {
    suspend fun getNotificationList(): Result<List<Notification>>

    suspend fun updateNotificationReadStatus(id: Int): Result<Unit>
}
