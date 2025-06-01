package com.bff.wespot.model.message.response

data class MessageRoom(
    val messageRoomId: Int = -1,
    val name: String = "",
    val thumbnail: String = "",
    val isBookmarked: Boolean = false,
    val isReceiverAnonymous: Boolean = false,
    val messageDetails: List<MessageDetail> = listOf(),
) {
    fun isLastMessage(message: MessageDetail): Boolean {
        return messageDetails.lastOrNull()?.id == message.id
    }

    fun getReceiverStatus(): String = if (isReceiverAnonymous) "익명" else "실명"
}
