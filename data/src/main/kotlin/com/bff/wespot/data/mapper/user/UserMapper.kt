package com.bff.wespot.data.mapper.user

import com.bff.wespot.data.remote.model.user.response.NotificationSettingDto
import com.bff.wespot.model.user.response.NotificationSetting
import com.bff.wespot.data.remote.model.user.response.ProfileCharacterDto
import com.bff.wespot.model.user.response.ProfileCharacter

internal fun NotificationSetting.toNotificationSettingDto() = NotificationSettingDto(
    isEnableVoteNotification = isEnableVoteNotification,
    isEnableMessageNotification = isEnableMessageNotification,
    isEnableMarketingNotification = isEnableMarketingNotification,
)

internal fun ProfileCharacter.toProfileCharacterDto() = ProfileCharacterDto(
    iconUrl = iconUrl,
    backgroundColor = backgroundColor,
)
