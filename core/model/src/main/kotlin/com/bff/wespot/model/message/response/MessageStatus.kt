package com.bff.wespot.model.message.response

data class MessageStatus(
    val isSendAllowed: Boolean,
    val countRemainingMessages: Int,
    val countUnReadMessages: Int,
) {
    companion object {
        private const val MESSAGE_COUNT_PER_DAY = 3
    }

    fun getReservedMessageCount(): Int = MESSAGE_COUNT_PER_DAY - countRemainingMessages

    fun hasReservedMessages() = countRemainingMessages < MESSAGE_COUNT_PER_DAY

    fun hasRemainingMessages() = countRemainingMessages > 0

    fun hasNoRemainingMessages() = countRemainingMessages == 0

    fun hasUnReadMessages() = countUnReadMessages > 0
}
