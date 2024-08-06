package com.bff.wespot.entire.screen.state

sealed class EntireAction {
    data object OnEntireScreenEntered : EntireAction()
    data object OnRevokeScreenEntered : EntireAction()
    data object OnProfileEditScreenEntered : EntireAction()
    data object OnBlockListScreenEntered : EntireAction()
    data object OnRevokeConfirmed : EntireAction()
    data object OnSignOutButtonClicked : EntireAction()
    data object OnRevokeButtonClicked : EntireAction()
    data object OnIntroductionEditDoneButtonClicked : EntireAction()
    data class OnProfileEditTextFieldFocused(val focused: Boolean) : EntireAction()
    data class OnIntroductionChanged(val introduction: String) : EntireAction()
    data object UnBlockMessage : EntireAction()
    data class OnUnBlockButtonClicked(val messageId: Int) : EntireAction()
    data class OnRevokeReasonSelected(val reason: String) : EntireAction()
}
