package com.bff.wespot.entire.screen.state.notification

sealed class NotificationSettingSideEffect {
    data object ShowMarketingDialog : NotificationSettingSideEffect()
    data object ShowMarketingResultDialog : NotificationSettingSideEffect()
}
