package com.bff.wespot.domain.repository.notification

interface NotificationRepository {
    suspend fun updateNotificationReadStatus(id: Int): Result<Unit>
}
