package com.bff.wespot.state

import com.bff.wespot.model.common.Restriction

data class MainUiState (
    val isPushNotificationNavigation: Boolean = true,
    val userId: String = "",
    val restriction: Restriction = Restriction.Empty,
)
