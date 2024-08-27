package com.bff.wespot.data.remote.model.user.request

import kotlinx.serialization.Serializable

@Serializable
data class FeatureNotificationSettingDto (
    val isEnableVoteNotification: Boolean,
    val isEnableMessageNotification: Boolean,
)
