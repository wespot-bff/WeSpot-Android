package com.bff.wespot.message.state

import androidx.paging.PagingData
import com.bff.wespot.message.model.ClickedMessageUiModel
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.message.response.SentMessage
import com.bff.wespot.model.user.response.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MessageUiState(
    val timePeriod: TimePeriod = TimePeriod.DAWN_TO_EVENING,
    val messageStatus: MessageStatus = MessageStatus(false, -1),
    val myProfile: Profile = Profile(),
    val hasUnReadMessage: Boolean = false,
    val receivedMessageList: Flow<PagingData<ReceivedMessage>> = flow { },
    val sentMessageList: Flow<PagingData<SentMessage>> = flow { },
    val clickedMessage: ClickedMessageUiModel = ClickedMessageUiModel(),
    val optionButtonClickedMessageId: Int = -1,
    val optionButtonClickedMessageType: MessageType = MessageType.RECEIVED,
    val messageOptionType: MessageOptionType = MessageOptionType.DELETE,
    val reservedMessageList: List<Message> = listOf(),
    val isLoading: Boolean = false,
)
