package com.bff.wespot.domain.repository.user

import com.bff.wespot.model.user.response.NotificationSetting
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.User

interface UserRepository {
    suspend fun getUserListByName(name: String, cursorId: Int): Result<List<User>>

    suspend fun getProfile(): Result<Profile>

    suspend fun getNotificationSetting(): Result<NotificationSetting>

    suspend fun updateNotificationSetting(notificationSetting: NotificationSetting): Result<Unit>
}
