package com.bff.wespot.message.state.send

sealed class SendSideEffect {
    data object NavigateToMessageScreen : SendSideEffect()
    data object NavigateToWriteScreen : SendSideEffect()
    data object NavigateToReceiverScreen : SendSideEffect()
    data object NavigateToEditScreen : SendSideEffect()
    data object PopBackStack : SendSideEffect()
}
