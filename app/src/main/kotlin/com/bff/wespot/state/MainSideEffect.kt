package com.bff.wespot.state

sealed class MainSideEffect {
    data object ShowNotificationSettingDialog: MainSideEffect()
}
