package com.bff.wespot.domain.repository.user

import com.bff.wespot.model.user.response.NotificationSetting
import com.bff.wespot.model.user.response.Profile

interface UserRepository {
    suspend fun getProfile(): Result<Profile>

    suspend fun getNotificationSetting(): Result<NotificationSetting>

    suspend fun setFeatureNotificationSetting(isEnableNotification: Boolean): Result<Unit>

    suspend fun updateNotificationSetting(notificationSetting: NotificationSetting): Result<Unit>
}
