package com.bff.wespot.message.state.storage

import com.bff.wespot.model.message.response.Message
import com.bff.wespot.ui.model.ToastState

sealed class StorageSideEffect {
    data class ShowToast(
        val toastState: ToastState,
    ) : StorageSideEffect()
    data object ShowOptionBottomSheet : StorageSideEffect()
    data object CloseOptionBottomSheet : StorageSideEffect()
    data object ShowBlockDialog : StorageSideEffect()
    data object CloseBlockDialog : StorageSideEffect()
    data class NavigateToMessageRoom(
        val message: Message,
    ) : StorageSideEffect()
}
