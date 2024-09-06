package com.bff.wespot.entire.screen.state.notification

sealed class NotificationSettingAction {
    data object OnNotificationSettingScreenEntered : NotificationSettingAction()
    data object OnNotificationSettingScreenExited : NotificationSettingAction()
    data object OnVoteNotificationSwitched : NotificationSettingAction()
    data object OnMessageNotificationSwitched : NotificationSettingAction()
    data object OnMarketingNotificationSwitched : NotificationSettingAction()
    data object SetMarketingNotificationEnable : NotificationSettingAction()
}
