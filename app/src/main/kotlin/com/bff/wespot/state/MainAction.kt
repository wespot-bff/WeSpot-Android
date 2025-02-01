package com.bff.wespot.state

import com.bff.wespot.MainScreenNavArgs
import com.bff.wespot.model.serverDriven.OnBoardingCategory

sealed class MainAction {
    data object OnMainScreenEntered : MainAction()
    data object OnNavigateByPushNotification : MainAction()
    data class OnEnteredByPushNotification(val data: MainScreenNavArgs) : MainAction()
    data class OnNotificationSet(val isEnableNotification: Boolean) : MainAction()
    data class CloseOnBoarding(val category: OnBoardingCategory) : MainAction()
}
