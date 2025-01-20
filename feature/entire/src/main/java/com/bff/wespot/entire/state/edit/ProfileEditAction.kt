package com.bff.wespot.entire.state.edit

sealed class ProfileEditAction {
    data object OnProfileEditDoneButtonClicked : ProfileEditAction()
    data object OnProfileEditScreenEntered : ProfileEditAction()
    data class OnProfileEditTextFieldFocused(val focused: Boolean) : ProfileEditAction()
    data class OnIntroductionChanged(val introduction: String) : ProfileEditAction()
    data object OnRequestDialogDismissed : ProfileEditAction()
    data object OnRequestDialogShown : ProfileEditAction()
    data class OnProfileImagePicked(val profilePath: String?) : ProfileEditAction()
    data class ChangeBottomSheetState(val isBottomSheetOpen: Boolean) : ProfileEditAction()
    data object OpenPicker : ProfileEditAction()
}
