package com.bff.wespot.state

import com.bff.wespot.MainScreenNavArgs

sealed class MainAction {
    data object OnNavigateByPushNotification : MainAction()
    data class OnMainScreenEntered(val appVersionName: String) : MainAction()
    data class OnEnteredByPushNotification(val data: MainScreenNavArgs) : MainAction()
    data class OnNotificationSet(val isEnableNotification: Boolean) : MainAction()
}
