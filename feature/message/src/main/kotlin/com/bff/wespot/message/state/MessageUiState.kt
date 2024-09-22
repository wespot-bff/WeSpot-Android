package com.bff.wespot.message.state

import com.bff.wespot.message.common.DEFAULT_MESSAGE_RECEIVE_TIME
import com.bff.wespot.message.common.DEFAULT_MESSAGE_START_TIME
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageStatus

data class MessageUiState(
    val timePeriod: TimePeriod = TimePeriod.DAWN_TO_EVENING,
    val messageStatus: MessageStatus = MessageStatus(false, -1, -1),
    val messageStartTime: String = DEFAULT_MESSAGE_START_TIME,
    val messageReceiveTime: String = DEFAULT_MESSAGE_RECEIVE_TIME,
    val reservedMessageList: List<Message> = listOf(),
    val isLoading: Boolean = false,
)
