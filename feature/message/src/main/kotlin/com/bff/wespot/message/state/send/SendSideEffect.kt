package com.bff.wespot.message.state.send

import androidx.annotation.StringRes

sealed class SendSideEffect {
    data object NavigateToMessage : SendSideEffect()
    data object NavigateToReservedMessage : SendSideEffect()
    data object ShowTimeoutDialog : SendSideEffect()
    data object CloseReserveDialog : SendSideEffect()
    data class ShowToast(
        @StringRes val message: Int,
    ) : SendSideEffect()
    data object DismissExitDialog : SendSideEffect()
}
