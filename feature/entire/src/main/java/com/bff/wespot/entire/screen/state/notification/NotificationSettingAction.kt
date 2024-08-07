package com.bff.wespot.entire.screen.state.notification

sealed class NotificationSettingAction {
    data object OnNotificationSettingScreenEntered : NotificationSettingAction()
    data object OnNotificationSettingScreenExited : NotificationSettingAction()
    data class OnVoteNotificationSwitched(val isSwitched: Boolean) : NotificationSettingAction()
    data class OnMessageNotificationSwitched(val isSwitched: Boolean) : NotificationSettingAction()
    data class OnEventNotificationSwitched(val isSwitched: Boolean) : NotificationSettingAction()
}
