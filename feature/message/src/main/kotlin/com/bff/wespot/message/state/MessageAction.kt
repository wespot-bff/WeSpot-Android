package com.bff.wespot.message.state

import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.message.response.SentMessage

sealed class MessageAction {
    data object OnHomeScreenEntered : MessageAction()
    data object OnReservedMessageScreenEntered : MessageAction()
    data object OnMessageBlockButtonClicked : MessageAction()
    data object OnMessageReportButtonClicked : MessageAction()
    data object OnMessageDeleteButtonClicked : MessageAction()
    data class OnStorageChipSelected(val messageType: MessageType) : MessageAction()
    data class OnMessageStorageScreenOpened(
        val messageId: Int,
        val type: MessageType,
    ) : MessageAction()
    data class OnReceivedMessageClicked(val message: ReceivedMessage) : MessageAction()
    data class OnSentMessageClicked(val message: SentMessage) : MessageAction()
    data class OnOptionButtonClicked(
        val messageId: Int,
        val messageType: MessageType,
    ) : MessageAction()
    data class OnOptionBottomSheetClicked(
        val messageOptionType: MessageOptionType,
    ) : MessageAction()
}
