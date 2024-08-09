package com.bff.wespot.entire.screen.state

import com.bff.wespot.model.user.response.ProfileCharacter

sealed class EntireAction {
    data object OnEntireScreenEntered : EntireAction()
    data object OnRevokeScreenEntered : EntireAction()
    data object OnCharacterEditScreenEntered : EntireAction()
    data object OnBlockListScreenEntered : EntireAction()
    data object OnRevokeConfirmed : EntireAction()
    data object OnSignOutButtonClicked : EntireAction()
    data object OnRevokeButtonClicked : EntireAction()
    data object OnIntroductionEditDoneButtonClicked : EntireAction()
    data object UnBlockMessage : EntireAction()
    data class OnProfileEditScreenEntered(val isCompleteEdit: Boolean) : EntireAction()
    data class OnProfileEditTextFieldFocused(val focused: Boolean) : EntireAction()
    data class OnIntroductionChanged(val introduction: String) : EntireAction()
    data class OnUnBlockButtonClicked(val messageId: Int) : EntireAction()
    data class OnRevokeReasonSelected(val reason: String) : EntireAction()
    data class OnCharacterEditDoneButtonClicked(val character: ProfileCharacter) : EntireAction()
}
