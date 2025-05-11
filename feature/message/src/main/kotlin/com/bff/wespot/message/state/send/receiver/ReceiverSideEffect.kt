package com.bff.wespot.message.state.send.receiver

import com.bff.wespot.message.state.send.MessageSendSideEffect

sealed interface ReceiverSideEffect : MessageSendSideEffect {
    data object NavigateToMessageWriteScreen : ReceiverSideEffect
    data object ShowAnonymousProfileModal : ReceiverSideEffect
    data object DismissAnonymousProfileModal : ReceiverSideEffect
    data object ShowProfileSelectBottomSheet : ReceiverSideEffect
    data object DismissProfileSelectBottomSheet : ReceiverSideEffect
    data object NavigateToMessage : ReceiverSideEffect
    data object DismissExitDialog : ReceiverSideEffect
    data object NavigateUp : ReceiverSideEffect
}
