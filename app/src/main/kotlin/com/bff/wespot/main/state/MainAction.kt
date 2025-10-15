package com.bff.wespot.main.state

import com.bff.wespot.model.notification.PushNotificationData
import com.bff.wespot.model.serverDriven.OnBoardingCategory

sealed class MainAction {
    data class OnMainScreenEntered(
        val appVersionName: String,
    ) : MainAction()
    data class OnEnteredByPushNotification(
        val data: PushNotificationData,
    ) : MainAction()
    data class OnNotificationSet(
        val isEnableNotification: Boolean,
    ) : MainAction()
    data class CloseOnBoarding(
        val category: OnBoardingCategory,
    ) : MainAction()
}
