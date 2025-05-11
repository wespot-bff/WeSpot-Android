package com.bff.wespot.message.state.send

import androidx.annotation.StringRes

sealed class SendSideEffect {
    data object NavigateToMessage : SendSideEffect()
    data class ShowToast(
        @StringRes val message: Int,
    ) : SendSideEffect()
    data object DismissExitDialog : SendSideEffect()
    data object NavigateToMessageSendScreen : SendSideEffect()
    data object NavigateToMessageWriteScreen : SendSideEffect()
    data object NavigateUp : SendSideEffect()
    data object ShowAnonymousProfileModal : SendSideEffect()
    data object DismissAnonymousProfileModal : SendSideEffect()
    data object ShowProfileSelectBottomSheet : SendSideEffect()
    data object DismissProfileSelectBottomSheet : SendSideEffect()
    data object CloseSendConfirmModal : SendSideEffect()
}
