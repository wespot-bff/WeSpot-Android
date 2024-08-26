package com.bff.wespot.state

import com.bff.wespot.MainScreenNavArgs

sealed class MainAction {
    data object OnMainScreenEntered : MainAction()
    data class OnNavigateByPushNotification(val data: MainScreenNavArgs) : MainAction()
    data class OnNotificationSet(val isEnableNotification: Boolean) : MainAction()
}
