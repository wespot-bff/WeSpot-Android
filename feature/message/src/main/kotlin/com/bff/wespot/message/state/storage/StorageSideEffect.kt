package com.bff.wespot.message.state.storage

import com.bff.wespot.ui.model.ToastState

sealed class StorageSideEffect {
    data class ShowToast(val toastState: ToastState) : StorageSideEffect()
    data object ShowReportMessageScreen : StorageSideEffect()
    data object CloseReportMessageScreen : StorageSideEffect()
    data object ShowOptionBottomSheet : StorageSideEffect()
    data object CloseOptionBottomSheet : StorageSideEffect()
    data object ShowOptionDialog : StorageSideEffect()
    data object CloseOptionDialog : StorageSideEffect()
}
