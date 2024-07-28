package com.bff.wespot.message.state

import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message

sealed class MessageAction {
    data object OnHomeScreenEntered : MessageAction()
    data object OnMessageStorageScreenEntered : MessageAction()
    data class OnStorageChipSelected(val messageType: MessageType) : MessageAction()
    data class OnMessageItemClicked(val message: Message) : MessageAction()
}
