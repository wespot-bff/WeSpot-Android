package com.bff.wespot.message.state.send

import com.bff.wespot.model.user.response.User

sealed class SendAction {
    data class OnSearchContentChanged(val content: String) : SendAction()
    data class OnUserSelected(val user: User) : SendAction()
    data class OnMessageChanged(val content: String) : SendAction()
    data class OnRandomNameToggled(val state: Boolean) : SendAction()
    data object SendMessage : SendAction()
    data object OnInviteFriendTextClicked : SendAction()
    data object NavigateToMessageHome : SendAction()
    data object OnReceiverScreenEntered : SendAction()
    data object OnWriteScreenEntered : SendAction()
    data object OnEditScreenEntered : SendAction()
}