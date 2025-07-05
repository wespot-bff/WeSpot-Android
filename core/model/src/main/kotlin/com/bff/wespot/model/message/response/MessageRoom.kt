package com.bff.wespot.model.message.response

data class MessageRoom(
    val messageRoomId: Int = -1,
    val name: String = "",
    val thumbnail: String = "",
    val isBookmarked: Boolean = false,
    val isReceiverAnonymous: Boolean = false,
    val messageDetails: List<MessageDetail> = listOf(),
) {
    fun showReplyButton(message: MessageDetail): Boolean {
        val isLastItem = messageDetails.lastOrNull()?.id == message.id

        if (message.isReceived) {
            return isLastItem
        } else {
            return isLastItem.not()
        }
    }

    fun isSingleMessage(): Boolean = messageDetails.size <= 1

    fun getReceiverStatus(): String = if (isReceiverAnonymous) "익명" else "실명"
}
