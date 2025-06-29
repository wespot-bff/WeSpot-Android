package com.bff.wespot.message.state.setting

sealed interface MessageUsageSettingAction {
    data object OnUsageSettingSwitched : MessageUsageSettingAction
    data object OnLifecycleStop : MessageUsageSettingAction
}
