package com.bff.wespot.entire.state

sealed class EntireAction {
    data object OnEntireScreenEntered : EntireAction()
    data object OnRevokeScreenEntered : EntireAction()
    data object OnRevokeConfirmed : EntireAction()
    data object OnSignOutButtonClicked : EntireAction()
    data object OnRevokeButtonClicked : EntireAction()
    data object OnSettingScreenEntered : EntireAction()
    data object OnInputRevokeReasonSelected : EntireAction()
    data class OnRevokeReasonSelected(val reason: String) : EntireAction()
    data class OnRevokeReasonChanged(val reason: String) : EntireAction()
}
