package com.bff.wespot.data.remote.source.notification

import com.bff.wespot.data.remote.model.notification.NotificationListDto

interface NotificationDataSource {
    suspend fun getNotification(): Result<NotificationListDto>

    suspend fun updateNotificationReadStatus(id: Int): Result<Unit>
}