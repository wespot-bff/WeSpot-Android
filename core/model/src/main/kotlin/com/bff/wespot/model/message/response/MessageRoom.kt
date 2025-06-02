package com.bff.wespot.model.message.response

data class MessageRoom(
    val messageRoomId: Int = -1,
    val name: String = "",
    val thumbnail: String = "",
    val isBookmarked: Boolean = false,
    val isReceiverAnonymous: Boolean = false,
    val messageDetails: List<MessageDetail> = listOf(),
) {
    /** 마지막 아이템이면서, 답장을 주고 받은 경우에만 노출한다. */
    fun showReplyButton(message: MessageDetail): Boolean {
        val isLastItem = messageDetails.lastOrNull()?.id == message.id
        return isLastItem && this.messageDetails.size > 1
    }

    fun isSingleMessage(): Boolean = messageDetails.size <= 1

    fun getReceiverStatus(): String = if (isReceiverAnonymous) "익명" else "실명"
}
