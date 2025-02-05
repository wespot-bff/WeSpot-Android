package com.bff.wespot.state

import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.serverDriven.OnBoardingCategory

sealed class MainAction {
    data class OnMainScreenEntered(val appVersionName: String) : MainAction()
    data class OnNotificationSet(val isEnableNotification: Boolean) : MainAction()
    data class OnEnteredByPushNotification(
        val type: NotificationType,
        val userId: String,
        val targetId: Int,
        val date: String,
        val appVersion: String,
    ) : MainAction()
    data object OnVersionUpdateDialogDismiss : MainAction()
    data object OnUpdateOverviewDismiss : MainAction()
    data class OnUpdateOverviewNavigate(val deepLink: String) : MainAction()
    data class CloseOnBoarding(val category: OnBoardingCategory) : MainAction()
}
