package com.bff.wespot.data.mapper.user

import com.bff.wespot.data.remote.model.user.response.NotificationSettingDto
import com.bff.wespot.model.user.response.NotificationSetting

internal fun NotificationSetting.toNotificationSettingDto() = NotificationSettingDto(
    isEnableVoteNotification = isEnableVoteNotification,
    isEnableMessageNotification = isEnableMessageNotification,
    isEnableMarketingNotification = isEnableMarketingNotification,
)
