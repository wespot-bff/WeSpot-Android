package com.bff.wespot.message.state.setting

sealed interface BlockedMessageAction {
    data class OnUnBlockButtonClicked(val id: Int) : BlockedMessageAction
    data object OnDialogUnBlockButtonClicked : BlockedMessageAction
    data object OnDialogCancelButtonClicked : BlockedMessageAction
}
