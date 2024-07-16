package com.bff.wespot.message.state

import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.MessageStatus

data class MessageUiState(
    val timePeriod: TimePeriod = TimePeriod.DAWN_TO_EVENING,
    val messageStatus: MessageStatus = MessageStatus(),
    val receivedMessageList: MessageList = MessageList(),
)
