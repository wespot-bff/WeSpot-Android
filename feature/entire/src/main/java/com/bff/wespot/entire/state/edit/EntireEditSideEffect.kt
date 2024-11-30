package com.bff.wespot.entire.state.edit

import com.bff.wespot.ui.model.ToastState

sealed class EntireEditSideEffect {
    data class ShowToast(val toastState: ToastState) : EntireEditSideEffect()
    data object OpenPicker : EntireEditSideEffect()
}
