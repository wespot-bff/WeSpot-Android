package com.bff.wespot.message.state

sealed class MessageAction {
    data object OnHomeScreenEntered : MessageAction()
    data class Navigation(val navigate: NavigationAction) : MessageAction()
}

sealed interface NavigationAction {
    data object PopBackStack : NavigationAction
    data class NavigateToReceiverScreen(val isEditing: Boolean) : NavigationAction
    data object NavigateToStorageScreen : NavigationAction
    data object NavigateToNotification : NavigationAction
}
