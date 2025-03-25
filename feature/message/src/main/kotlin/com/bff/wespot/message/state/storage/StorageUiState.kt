package com.bff.wespot.message.state.storage

import androidx.paging.PagingData
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.MessageContent
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.message.response.SentMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class StorageUiState(
    val receivedMessageList: Flow<PagingData<ReceivedMessage>> = flow { },
    val sentMessageList: Flow<PagingData<SentMessage>> = flow { },
    val messageDialogContent: MessageContent = MessageContent(),
    val optionButtonClickedMessageId: Int = -1,
    val optionButtonClickedMessageType: MessageType = MessageType.RECEIVED,
    val messageOptionType: MessageOptionType = MessageOptionType.DELETE,
    val isLoading: Boolean = false,
)
