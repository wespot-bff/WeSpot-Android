package com.bff.wespot.entire.state.notification

sealed class NotificationSettingSideEffect {
    data object ShowMarketingDialog : NotificationSettingSideEffect()
    data object ShowMarketingResultDialog : NotificationSettingSideEffect()
}
