package com.bff.wespot.message.state.storage

import com.bff.wespot.model.message.response.Message

sealed class StorageAction {
    data class OnStorageChipSelected(
        val screenIndex: Int,
    ) : StorageAction()
    data class OnMessageClicked(
        val message: Message,
    ) : StorageAction()
    data class OnOptionButtonClicked(
        val message: Message,
    ) : StorageAction()
    data object OnBlockBottomSheetItemClicked : StorageAction()
    data class OnBookmarkBottomSheetItemClicked(
        val fromBookmarkScreen: Boolean,
    ) : StorageAction()
    data object OnOptionBottomSheetClosed : StorageAction()
    data object OnBlockButtonClicked : StorageAction()
    data object OnBlockDialogClosed : StorageAction()
}
