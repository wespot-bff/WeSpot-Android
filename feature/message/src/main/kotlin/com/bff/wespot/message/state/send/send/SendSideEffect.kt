package com.bff.wespot.message.state.send.send

import androidx.annotation.StringRes
import com.bff.wespot.message.state.send.MessageSendSideEffect

sealed interface SendSideEffect : MessageSendSideEffect {
    data object NavigateToMessageWriteScreen : SendSideEffect
    data class ShowToast(
        @StringRes val message: Int,
    ) : SendSideEffect
    data object ShowAnonymousProfileModal : SendSideEffect
    data object DismissAnonymousProfileModal : SendSideEffect
    data object CloseSendConfirmModal : SendSideEffect
    data object NavigateToMessage : SendSideEffect
    data object DismissExitDialog : SendSideEffect
    data object NavigateUp : SendSideEffect
}
