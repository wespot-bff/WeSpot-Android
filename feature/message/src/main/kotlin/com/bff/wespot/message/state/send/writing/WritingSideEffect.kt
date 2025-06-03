package com.bff.wespot.message.state.send.writing

import com.bff.wespot.message.state.send.MessageSendSideEffect

sealed interface WritingSideEffect : MessageSendSideEffect {
    data object NavigateToMessageSendScreen : WritingSideEffect
    data object NavigateToMessage : WritingSideEffect
    data object DismissExitDialog : WritingSideEffect
    data object NavigateUp : WritingSideEffect
}
