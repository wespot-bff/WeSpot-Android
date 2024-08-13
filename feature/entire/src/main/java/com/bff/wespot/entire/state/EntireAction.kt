package com.bff.wespot.entire.state

sealed class EntireAction {
    data object OnEntireScreenEntered : EntireAction()
    data object OnRevokeScreenEntered : EntireAction()
    data object OnBlockListScreenEntered : EntireAction()
    data object OnRevokeConfirmed : EntireAction()
    data object OnSignOutButtonClicked : EntireAction()
    data object OnRevokeButtonClicked : EntireAction()
    data object UnBlockMessage : EntireAction()
    data class OnUnBlockButtonClicked(val messageId: Int) : EntireAction()
    data class OnRevokeReasonSelected(val reason: String) : EntireAction()
}
