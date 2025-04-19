package com.bff.wespot.message.state.storage

import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.response.Message

sealed class StorageAction {
    data object OnMessageBlockButtonClicked : StorageAction()
    data object OnMessageDeleteButtonClicked : StorageAction()
    data object OnMessageReportButtonClicked : StorageAction()
    data class OnStorageChipSelected(val screenIndex: Int) : StorageAction()
    data class OnMessageClicked(val message: Message) : StorageAction()
    data class OnOptionButtonClicked(val messageId: Int) : StorageAction()
    data class OnBookmarkButtonClicked(val messageId: Int) : StorageAction()
    data class OnOptionBottomSheetClicked(val messageOptionType: MessageOptionType) : StorageAction()
    data class OnPushNotificationNavigated(val messageId: Int) : StorageAction()
    data object OnOptionDialogClosed : StorageAction()
    data object OnOptionBottomSheetClosed : StorageAction()
    data object OnMessageReportScreenClosed : StorageAction()
}
