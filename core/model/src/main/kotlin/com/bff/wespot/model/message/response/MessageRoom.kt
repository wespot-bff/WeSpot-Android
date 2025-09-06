package com.bff.wespot.model.message.response

data class MessageRoom(
    val messageRoomId: Int = -1,
    val name: String = "",
    val thumbnail: String = "",
    val isBookmarked: Boolean = false,
    val isReceiverAnonymous: Boolean = false,
    val messageDetails: List<MessageDetail> = listOf(),
) {
    fun isLastReceivedMessage(message: MessageDetail): Boolean {
        val isLastItem = messageDetails.lastOrNull()?.id == message.id
        return isLastItem && message.isReceived
    }

    fun isSingleMessage(): Boolean = messageDetails.size <= 1

    fun getReceiverStatus(): String = if (isReceiverAnonymous) "익명" else "실명"

    /** 받은 쪽지가 한개 인 경우, 처음 답장을 보내는 것으로 간주 */
    fun isFirstReplyContext(): Boolean {
        val receivedMessageCount = messageDetails.count { it.isReceived }
        return receivedMessageCount == 1
    }
}
