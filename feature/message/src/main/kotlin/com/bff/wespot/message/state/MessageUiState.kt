package com.bff.wespot.message.state

import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.user.response.Profile

data class MessageUiState(
    val timePeriod: TimePeriod = TimePeriod.DAWN_TO_EVENING,
    val messageStatus: MessageStatus = MessageStatus(false, -1),
    val receivedMessageList: MessageList = MessageList(listOf(), true),
    val myProfile: Profile = Profile(),
    val sentMessageList: MessageList = MessageList(listOf(), true),
    val clickedMessage: Message = Message(),
    val optionButtonClickedMessage: Message = Message(),
    val messageOptionType: MessageOptionType = MessageOptionType.DELETE,
    val reservedMessageList: List<Message> = listOf(),
    val isLoading: Boolean = false,
)
