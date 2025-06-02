package com.bff.wespot.message.state

sealed class MessageAction {
    data class OnTabSelected(val index: Int) : MessageAction()
    data object OnLifecycleStart : MessageAction()
    data object OnLifecycleStop : MessageAction()
}
