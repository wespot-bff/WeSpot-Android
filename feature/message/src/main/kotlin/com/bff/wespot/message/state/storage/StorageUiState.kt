package com.bff.wespot.message.state.storage

import androidx.paging.PagingData
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.model.message.response.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class StorageUiState(
    val messageList: Flow<PagingData<Message>> = flow { },
    val optionButtonClickedMessageId: Int = -1,
    val messageOptionType: MessageOptionType = MessageOptionType.DELETE,
    val isLoading: Boolean = false,
)
