package com.bff.wespot.state

import com.bff.wespot.model.VersionUpdateType
import com.bff.wespot.model.common.Restriction
import com.bff.wespot.model.notification.NotificationType

data class MainUiState (
    val userId: String = "",
    val restriction: Restriction = Restriction.Empty,
    val versionUpdateType: VersionUpdateType = VersionUpdateType.USABILITY_IMPROVEMENT,
    val notificationType: NotificationType = NotificationType.IDLE,
    val kakaoChannel: String,
    val playStoreLink: String,
)
