package com.bff.wespot.message.state

sealed class MessageAction {
    data object OnHomeScreenEntered : MessageAction()
    data object OnReservedMessageScreenEntered : MessageAction()
}
