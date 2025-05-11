package com.bff.wespot.message.state.send

import com.bff.wespot.message.model.AnonymousProfile
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.model.user.response.User

sealed class SendAction {
    /** 쪽지 수신자 선택 화면 */
    data object OnReceiverScreenEntered : SendAction()
    data class OnSearchContentChanged(val content: String) : SendAction()
    data class OnUserSelected(val user: User) : SendAction()
    data object OnSelectDoneButtonClicked : SendAction()

    /** 쪽지 내용 작성 화면 */
    data object OnWriteScreenEntered : SendAction()
    data class OnMessageChanged(val content: String) : SendAction()
    data object OnProfileBottomSheetClosed : SendAction()
    data class OnProfileBottomSheetSelected(val senderProfile: SenderProfile) : SendAction()
    data object OnProfileAddButtonClicked : SendAction()
    data object OnWriteDoneButtonClicked : SendAction()

    /** 쪽지 전송 화면 */
    data object OnSendButtonClicked : SendAction()
    data object OnSenderClicked : SendAction()

    /** 공통 */
    data object OnMessageScreenEntered : SendAction()
    data object OnExitDialogExitButtonClicked : SendAction()
    data object OnExitDialogCancelButtonClicked : SendAction()
    data object OnTopBarNavigateButtonClicked : SendAction()
    data class OnAnonymousProfileSelected(val profile: AnonymousProfile) : SendAction()
    data object OnAnonymousProfileModalDismiss : SendAction()
}
