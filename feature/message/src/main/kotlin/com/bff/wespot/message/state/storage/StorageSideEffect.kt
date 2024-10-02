package com.bff.wespot.message.state.storage

import com.bff.wespot.ui.model.ToastState

sealed class StorageSideEffect {
    data class ShowToast(val toastState: ToastState) : StorageSideEffect()
    data object ShowMessageDialog : StorageSideEffect()
}
