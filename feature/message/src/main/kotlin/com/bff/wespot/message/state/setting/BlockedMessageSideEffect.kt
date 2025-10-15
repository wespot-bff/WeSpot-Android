package com.bff.wespot.message.state.setting

import com.bff.wespot.ui.model.ToastState

sealed interface BlockedMessageSideEffect {
    data object ShowDialog : BlockedMessageSideEffect
    data object DismissDialog : BlockedMessageSideEffect
    data class ShowToast(
        val toastState: ToastState,
    ) : BlockedMessageSideEffect
}
