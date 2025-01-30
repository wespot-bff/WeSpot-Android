package com.bff.wespot.state.featureoverview

import com.bff.wespot.model.notification.NotificationType

sealed interface FeatureOverviewAction {
    data class OnFeatureOverViewDialogShow(
        val notificationType: NotificationType,
    ): FeatureOverviewAction
    data class OnNavigateButtonClicked(val deepLink: String) : FeatureOverviewAction
    data object OnDismissButtonClicked : FeatureOverviewAction
}
