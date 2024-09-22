package com.bff.wespot.message.state

sealed class MessageAction {
    data object StartTimeTracking : MessageAction()
    data object CancelTimeTracking : MessageAction()
    data object OnReservedMessageScreenEntered : MessageAction()
}
