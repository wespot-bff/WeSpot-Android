package com.bff.wespot.message.state

sealed class MessageSideEffect {
    data object ShowMessageDialog : MessageSideEffect()
    data class ShowToast(val message: String) : MessageSideEffect()
}
