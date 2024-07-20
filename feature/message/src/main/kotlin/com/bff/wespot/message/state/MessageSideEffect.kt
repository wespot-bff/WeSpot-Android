package com.bff.wespot.message.state

sealed class MessageSideEffect {
    data class Error(val exception: Throwable) : MessageSideEffect()
    data object NavigateToSendScreen : MessageSideEffect()
    data object NavigateToStorageScreen : MessageSideEffect()
    data object NavigateToNotification : MessageSideEffect()
}
