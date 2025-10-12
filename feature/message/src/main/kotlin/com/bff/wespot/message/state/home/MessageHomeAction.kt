package com.bff.wespot.message.state.home

sealed class MessageHomeAction {
    data object OnScreenEntered : MessageHomeAction()
    data object OnLifecycleResume : MessageHomeAction()
    data object OnLifecycleStop : MessageHomeAction()
    data object OnMessageUsageSettingConfirmed : MessageHomeAction()
    data object OnMessageUsageSettingDialogDismissed : MessageHomeAction()
}
