package com.bff.wespot.message.state

sealed class MessageAction {
    data object OnMessageHomeScreenEntered : MessageAction()
}
