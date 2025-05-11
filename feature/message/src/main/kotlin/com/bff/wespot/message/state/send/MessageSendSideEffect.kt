package com.bff.wespot.message.state.send

import androidx.annotation.StringRes

sealed class MessageSendSideEffect {
    data object NavigateToMessage : MessageSendSideEffect()
    data class ShowToast(
        @StringRes val message: Int,
    ) : MessageSendSideEffect()
    data object DismissExitDialog : MessageSendSideEffect()
    data object NavigateToMessageSendScreen : MessageSendSideEffect()
    data object NavigateToMessageWriteScreen : MessageSendSideEffect()
    data object NavigateUp : MessageSendSideEffect()
    data object ShowAnonymousProfileModal : MessageSendSideEffect()
    data object DismissAnonymousProfileModal : MessageSendSideEffect()
    data object ShowProfileSelectBottomSheet : MessageSendSideEffect()
    data object DismissProfileSelectBottomSheet : MessageSendSideEffect()
    data object CloseSendConfirmModal : MessageSendSideEffect()
}
