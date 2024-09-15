package com.bff.wespot.message.state.send

sealed class SendSideEffect {
    data object NavigateToMessage : SendSideEffect()
    data object NavigateToReservedMessage : SendSideEffect()
    data object ShowTimeoutDialog : SendSideEffect()
    data object CloseReserveDialog : SendSideEffect()
}
