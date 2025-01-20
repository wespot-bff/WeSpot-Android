package com.bff.wespot.data.remote.source.user

import com.bff.wespot.data.remote.model.user.request.FeatureNotificationSettingDto
import com.bff.wespot.data.remote.model.user.request.ProfileUpdateDto
import com.bff.wespot.data.remote.model.user.response.NotificationSettingDto
import com.bff.wespot.data.remote.model.user.response.ProfileDto
import com.bff.wespot.data.remote.model.user.response.UserListDto

interface UserDataSource {
    suspend fun getUserListByName(name: String, cursorId: Int?): Result<UserListDto>

    suspend fun getProfile(): Result<ProfileDto>

    suspend fun getNotificationSetting(): Result<NotificationSettingDto>

    suspend fun setFeatureNotificationSetting(
        featureNotificationSettingDto: FeatureNotificationSettingDto,
    ): Result<Unit>

    suspend fun updateNotificationSetting(notificationSetting: NotificationSettingDto): Result<Unit>

    suspend fun updateProfile(profileUpdateDto: ProfileUpdateDto): Result<Unit>
}
