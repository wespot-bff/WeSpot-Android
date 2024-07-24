package com.bff.wespot.message.state.send

sealed class SendSideEffect {
    data object NavigateToMessage : SendSideEffect()
    data object ShowTimeoutDialog : SendSideEffect()
}