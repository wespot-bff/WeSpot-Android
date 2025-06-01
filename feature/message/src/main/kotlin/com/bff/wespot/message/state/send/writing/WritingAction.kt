package com.bff.wespot.message.state.send.writing

import com.bff.wespot.message.screen.send.MessageWriteScreenArgs

sealed interface WritingAction {
    data class OnWriteScreenEntered(val args: MessageWriteScreenArgs) : WritingAction
    data class OnMessageChanged(val content: String) : WritingAction
    data object OnWriteDoneButtonClicked : WritingAction
    data object OnExitDialogExitButtonClicked : WritingAction
    data object OnExitDialogCancelButtonClicked : WritingAction
    data object OnTopBarNavigateButtonClicked : WritingAction
    data object OnReplyButtonClicked : WritingAction
    data object OnReplyCancelButtonClicked : WritingAction
}
