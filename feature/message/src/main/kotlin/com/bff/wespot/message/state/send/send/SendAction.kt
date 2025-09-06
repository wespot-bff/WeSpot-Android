package com.bff.wespot.message.state.send.send

import com.bff.wespot.message.model.AnonymousProfile

sealed interface SendAction {
    data object OnMessageContentClick : SendAction
    data object OnSendButtonClicked : SendAction
    data object OnSenderClicked : SendAction
    data object OnExitDialogExitButtonClicked : SendAction
    data object OnExitDialogCancelButtonClicked : SendAction
    data object OnTopBarNavigateButtonClicked : SendAction
    data class OnAnonymousProfileSelected(val profile: AnonymousProfile) : SendAction
    data object OnAnonymousProfileModalDismiss : SendAction
}
