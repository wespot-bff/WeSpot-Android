package com.bff.wespot.message.state

import com.bff.wespot.model.ToastState

sealed class MessageSideEffect {
    data class ShowToast(val toastState: ToastState) : MessageSideEffect()
}
