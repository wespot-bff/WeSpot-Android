package com.bff.wespot.message.state.setting

sealed interface MessageUsageSettingAction {
    data object OnReceivedSettingSwitched : MessageUsageSettingAction
    data object OnLifecycleStop : MessageUsageSettingAction
}
