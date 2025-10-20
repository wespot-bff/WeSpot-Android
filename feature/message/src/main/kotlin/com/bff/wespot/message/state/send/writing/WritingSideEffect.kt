package com.bff.wespot.message.state.send.writing

import com.bff.wespot.message.state.send.MessageSendSideEffect
import com.bff.wespot.ui.model.ToastState

sealed interface WritingSideEffect : MessageSendSideEffect {
    data object NavigateToMessageSendScreen : WritingSideEffect
    data object NavigateToMessage : WritingSideEffect
    data object DismissExitDialog : WritingSideEffect
    data object ShowReplyDialog : WritingSideEffect
    data object DismissReplyDialog : WritingSideEffect
    data object NavigateUp : WritingSideEffect
    data class ShowToast(
        val toastState: ToastState,
    ) : WritingSideEffect
}
