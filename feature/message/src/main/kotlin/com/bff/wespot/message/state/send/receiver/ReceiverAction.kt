package com.bff.wespot.message.state.send.receiver

import com.bff.wespot.message.model.AnonymousProfile
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.model.user.response.User

sealed interface ReceiverAction {
    data object OnReceiverScreenEntered : ReceiverAction
    data object OnProfileBottomSheetClosed : ReceiverAction
    data class OnProfileBottomSheetSelected(
        val senderProfile: SenderProfile,
    ) : ReceiverAction
    data object OnProfileAddButtonClicked : ReceiverAction
    data class OnSearchContentChanged(
        val content: String,
    ) : ReceiverAction
    data class OnUserSelected(
        val user: User,
    ) : ReceiverAction
    data object OnSelectDoneButtonClicked : ReceiverAction
    data object OnExitDialogExitButtonClicked : ReceiverAction
    data object OnExitDialogCancelButtonClicked : ReceiverAction
    data object OnTopBarNavigateButtonClicked : ReceiverAction
    data class OnAnonymousProfileSelected(
        val profile: AnonymousProfile,
    ) : ReceiverAction
    data object OnAnonymousProfileModalDismiss : ReceiverAction
}
