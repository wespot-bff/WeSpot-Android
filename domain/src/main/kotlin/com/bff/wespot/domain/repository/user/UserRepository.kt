package com.bff.wespot.domain.repository.user

import com.bff.wespot.model.user.response.NotificationSetting
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.ProfileCharacter

interface UserRepository {
    suspend fun getProfile(): Result<Profile>

    suspend fun getNotificationSetting(): Result<NotificationSetting>

    suspend fun updateNotificationSetting(notificationSetting: NotificationSetting): Result<Unit>

    suspend fun updateIntroduction(introduction: String): Result<Unit>

    suspend fun updateCharacter(character: ProfileCharacter): Result<Unit>
}
