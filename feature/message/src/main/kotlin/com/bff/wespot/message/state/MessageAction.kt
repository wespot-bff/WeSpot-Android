package com.bff.wespot.message.state

sealed class MessageAction {
    data object OnLifecycleStart : MessageAction()
    data object OnLifecycleStop : MessageAction()
    data object OnMessageUsageSettingConfirmed : MessageAction()
    data object OnMessageUsageSettingDialogDismissed : MessageAction()
}
