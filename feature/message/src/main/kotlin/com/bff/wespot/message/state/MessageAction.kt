package com.bff.wespot.message.state

sealed class MessageAction {
    data object OnMessageHomeScreenEntered : MessageAction()
    data object OnLifecycleStart : MessageAction()
    data object OnLifecycleStop : MessageAction()
}
