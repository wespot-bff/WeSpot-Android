package com.bff.wespot.entire.screen.state

import com.bff.wespot.model.ToastState

sealed class EntireSideEffect {
    data object NavigateToAuth : EntireSideEffect()
    data object NavigateToEntire : EntireSideEffect()
    data class ShowToast(val toastState: ToastState) : EntireSideEffect()
}
