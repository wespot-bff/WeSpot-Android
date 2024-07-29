package com.bff.wespot.message.state

import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message

sealed class MessageAction {
    data object OnHomeScreenEntered : MessageAction()
    data object OnReservedMessageScreenEntered : MessageAction()
    data class OnStorageChipSelected(val messageType: MessageType) : MessageAction()
    data class OnMessageItemClicked(val message: Message) : MessageAction()
    data class OnOptionButtonClicked(val message: Message) : MessageAction()
    data class OnOptionBottomSheetClicked(
        val messageOptionType: MessageOptionType,
    ) : MessageAction()
    data class OnMessageDeleteButtonClicked(val messageId: Int) : MessageAction()
    data class OnMessageBlockButtonClicked(val messageId: Int) : MessageAction()
    data class OnMessageReportButtonClicked(val messageId: Int) : MessageAction()
}
