package com.bff.wespot.state

import com.bff.wespot.MainScreenNavArgs

sealed class MainAction {
    data object OnMainScreenEntered : MainAction()
    data object OnNavigateByPushNotification : MainAction()
    data class OnEnteredByPushNotification(val data: MainScreenNavArgs) : MainAction()
    data class OnNotificationSet(val isEnableNotification: Boolean) : MainAction()
}
