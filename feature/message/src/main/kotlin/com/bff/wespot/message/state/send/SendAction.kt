package com.bff.wespot.message.state.send

import com.bff.wespot.model.user.response.User

sealed class SendAction {
    data object OnReceiverScreenEntered : SendAction()
    data object OnWriteScreenEntered : SendAction()
    data class OnSearchContentChanged(val content: String) : SendAction()
    data class OnUserSelected(val user: User) : SendAction()
    data class OnMessageChanged(val content: String) : SendAction()
    data class OnRandomNameToggled(val state: Boolean) : SendAction()
    data object SendMessage : SendAction()
    data object OnInviteFriendTextClicked : SendAction()
    data class Navigation(val navigate: NavigationAction) : SendAction()
}

sealed interface NavigationAction {
    data object PopBackStack : NavigationAction
    data object NavigateToMessageScreen : NavigationAction
    data class NavigateToWriteScreen(val isEditing: Boolean) : NavigationAction
    data class NavigateToReceiverScreen(val isEditing: Boolean) : NavigationAction
    data object NavigateToEditScreen : NavigationAction
}
