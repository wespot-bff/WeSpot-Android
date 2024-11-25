package com.bff.wespot.entire.state.edit

sealed class EntireEditAction {
    data object OnCharacterEditScreenEntered : EntireEditAction()
    data object OnProfileEditDoneButtonClicked : EntireEditAction()
    data class OnProfileEditScreenEntered(val isCompleteEdit: Boolean) : EntireEditAction()
    data class OnProfileEditTextFieldFocused(val focused: Boolean) : EntireEditAction()
    data class OnIntroductionChanged(val introduction: String) : EntireEditAction()
    data object OnRequestDialogDismissed : EntireEditAction()
    data object OnRequestDialogShown : EntireEditAction()
    data class OnProfileImagePicked(val profilePath: String?) : EntireEditAction()
    data class ChangeBottomSheetState(val isBottomSheetOpen: Boolean) : EntireEditAction()
}
