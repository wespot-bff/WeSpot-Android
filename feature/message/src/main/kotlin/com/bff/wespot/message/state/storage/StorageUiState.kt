package com.bff.wespot.message.state.storage

import com.bff.wespot.model.message.response.Message

data class StorageUiState(
    val selectedChipIndex: Int = 0,
    val messageList: List<Message> = listOf(),
    val optionButtonClickedMessage: Message = Message(),
    val isLoading: Boolean = false,
) {
    fun hasNoBookmarkedMessages() = messageList.count { it.isBookmarked } == 0
}
