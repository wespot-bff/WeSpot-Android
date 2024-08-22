package com.bff.wespot.state

sealed class MainAction {
    data object OnNavigateByPushNotification : MainAction()
    data object OnMainScreenEntered : MainAction()
}
