package com.bff.wespot.message.state.setting

sealed interface MessageNotificationSettingAction {
    data object OnNotificationSettingSwitched : MessageNotificationSettingAction
    data object OnLifecycleStop : MessageNotificationSettingAction
}
