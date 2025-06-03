package com.bff.wespot.message.state.send.writing

sealed interface WritingAction {
    data object OnWriteScreenEntered : WritingAction
    data class OnMessageChanged(val content: String) : WritingAction
    data object OnWriteDoneButtonClicked : WritingAction
    data object OnExitDialogExitButtonClicked : WritingAction
    data object OnExitDialogCancelButtonClicked : WritingAction
    data object OnTopBarNavigateButtonClicked : WritingAction
}
