package com.bff.wespot.model.message.response

data class MessageStatus(
    val isSendAllowed: Boolean,
    val remainingMessages: Int,
) {
    companion object {
        private const val MESSAGE_COUNT_PER_DAY = 3
    }

    fun getReservedMessageCount(): Int = MESSAGE_COUNT_PER_DAY - remainingMessages

    fun hasReservedMessages() = remainingMessages < MESSAGE_COUNT_PER_DAY

    fun hasRemainingMessages() = remainingMessages > 0

    fun hasNoRemainingMessages() = remainingMessages == 0
}
