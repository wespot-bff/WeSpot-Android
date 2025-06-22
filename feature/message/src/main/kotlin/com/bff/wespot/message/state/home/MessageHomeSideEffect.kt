package com.bff.wespot.message.state.home

sealed interface MessageHomeSideEffect {
    data object ShowMessageUsageSettingDialog : MessageHomeSideEffect
    data object DismissMessageUsageSettingDialog : MessageHomeSideEffect
    data object NavigateToMessageUsageSettingScreen : MessageHomeSideEffect
}
