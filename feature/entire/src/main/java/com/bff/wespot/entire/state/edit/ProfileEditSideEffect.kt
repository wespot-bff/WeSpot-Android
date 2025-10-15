package com.bff.wespot.entire.state.edit

import com.bff.wespot.ui.model.ToastState

sealed class ProfileEditSideEffect {
    data class ShowToast(
        val toastState: ToastState,
    ) : ProfileEditSideEffect()
    data object OpenPicker : ProfileEditSideEffect()
}
