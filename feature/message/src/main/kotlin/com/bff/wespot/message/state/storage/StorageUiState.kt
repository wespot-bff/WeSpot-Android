package com.bff.wespot.message.state.storage

import androidx.paging.PagingData
import com.bff.wespot.message.common.DEFAULT_MESSAGE_RECEIVE_TIME
import com.bff.wespot.message.common.DEFAULT_MESSAGE_START_TIME
import com.bff.wespot.message.model.ClickedMessageUiModel
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.message.response.ReceivedMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class StorageUiState(
    val isTimePeriodEveningToNight: Boolean = false,
    val receivedMessageList: Flow<PagingData<ReceivedMessage>> = flow { },
    val sentMessageList: Flow<PagingData<Message>> = flow { },
    val clickedMessage: ClickedMessageUiModel = ClickedMessageUiModel(),
    val optionButtonClickedMessageId: Int = -1,
    val optionButtonClickedMessageType: MessageType = MessageType.RECEIVED,
    val messageOptionType: MessageOptionType = MessageOptionType.DELETE,
    val isLoading: Boolean = false,
    val messageStartTime: String = DEFAULT_MESSAGE_START_TIME,
    val messageReceiveTime: String = DEFAULT_MESSAGE_RECEIVE_TIME,
    val messageStatus: MessageStatus = MessageStatus(false, -1, -1),
)
