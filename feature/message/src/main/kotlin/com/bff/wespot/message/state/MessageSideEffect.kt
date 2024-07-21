package com.bff.wespot.message.state

sealed class MessageSideEffect {
    data object PopBackStack : MessageSideEffect()
    data class NavigateToReceiverScreen(val isEditing: Boolean) : MessageSideEffect()
    data object NavigateToStorageScreen : MessageSideEffect()
    data object NavigateToNotification : MessageSideEffect()
}
