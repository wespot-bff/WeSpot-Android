package com.bff.wespot.message.state

sealed interface MessageSideEffect {
    data object ShowMessageUsageSettingDialog : MessageSideEffect
    data object DismissMessageUsageSettingDialog : MessageSideEffect
    data object NavigateToMessageUsageSettingScreen : MessageSideEffect
}
