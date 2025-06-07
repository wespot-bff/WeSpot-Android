package com.bff.wespot.message.state.setting

sealed interface BlockedMessageAction {
    data object OnScreenEntered : BlockedMessageAction
    data class OnUnBlockButtonClicked(val id: Int) : BlockedMessageAction
}
