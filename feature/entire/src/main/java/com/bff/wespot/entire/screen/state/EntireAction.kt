package com.bff.wespot.entire.screen.state

sealed class EntireAction {
    data object OnEntireScreenEntered : EntireAction()
    data object OnRevokeScreenEntered : EntireAction()
    data object OnRevokeConfirmed : EntireAction()
    data object OnSignOutButtonClicked : EntireAction()
    data object OnRevokeButtonClicked : EntireAction()
    data class OnRevokeReasonSelected(val reason: String) : EntireAction()
}
