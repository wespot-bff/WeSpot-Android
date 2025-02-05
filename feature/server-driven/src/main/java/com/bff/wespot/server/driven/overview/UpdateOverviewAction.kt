package com.bff.wespot.server.driven.overview

import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.serverDriven.ClickAction

sealed interface UpdateOverviewAction {
    data class OnScreenEntered(
        val notificationType: NotificationType,
    ) : UpdateOverviewAction
    data object OnNavigateUpButtonClicked : UpdateOverviewAction
    data class OnButtonClicked(val clickAction: ClickAction) : UpdateOverviewAction
}
