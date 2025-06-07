package com.bff.wespot.message.state.setting

import com.bff.wespot.ui.model.ToastState

sealed interface BlockedMessageSideEffect {
    data class ShowToast(val toastState: ToastState) : BlockedMessageSideEffect
}
