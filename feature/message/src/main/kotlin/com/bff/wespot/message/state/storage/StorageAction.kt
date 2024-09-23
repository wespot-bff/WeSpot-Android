package com.bff.wespot.message.state.storage

import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.ReceivedMessage

sealed class StorageAction {
    data object StartTimeTracking : StorageAction()
    data object CancelTimeTracking : StorageAction()
    data object OnMessageBlockButtonClicked : StorageAction()
    data object OnMessageReportButtonClicked : StorageAction()
    data object OnMessageDeleteButtonClicked : StorageAction()
    data class OnStorageChipSelected(val messageType: MessageType) : StorageAction()
    data class OnReceivedMessageClicked(val message: ReceivedMessage) : StorageAction()
    data class OnSentMessageClicked(val message: Message) : StorageAction()
    data class OnOptionButtonClicked(
        val messageId: Int,
        val messageType: MessageType,
    ) : StorageAction()
    data class OnOptionBottomSheetClicked(
        val messageOptionType: MessageOptionType,
    ) : StorageAction()
    data class OnMessageStorageScreenOpened(
        val messageId: Int,
        val type: MessageType,
    ) : StorageAction()
}
