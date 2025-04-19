package com.bff.wespot.message.state.send

import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.model.user.response.User

sealed class SendAction {
    data class OnSearchContentChanged(val content: String) : SendAction()
    data class OnUserSelected(val user: User) : SendAction()
    data class OnMessageChanged(val content: String) : SendAction()
    data object OnSendButtonClicked : SendAction()
    data object OnReceiverScreenEntered : SendAction()
    data object OnWriteScreenEntered : SendAction()
    data object OnMessageScreenEntered : SendAction()
    data object OnExitDialogExitButtonClicked : SendAction()
    data object OnExitDialogCancelButtonClicked : SendAction()
    data object OnTopBarNavigateButtonClicked : SendAction()
    data object OnProfileBottomSheetClosed : SendAction()
    data object OnProfileAddButtonClicked : SendAction()

    data class OnProfileSelected(val senderProfile: SenderProfile) : SendAction()
    data object OnMessageSendScreenEntered : SendAction()
    data object OnProfileImageClicked : SendAction()
    data class OnProfileNameChanged(val name: String) : SendAction()
    data object OnProfileCreatorModalClosed : SendAction()
    data object OnPickerOpenOptionClicked : SendAction()
    data object OnRemoveProfileOptionClicked : SendAction()
    data object OnProfileOptionSheetClosed : SendAction()
    data class OnProfileImagePicked(val profilePath: String) : SendAction()
}
