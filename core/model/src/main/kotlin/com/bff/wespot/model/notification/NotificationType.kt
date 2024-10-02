package com.bff.wespot.model.notification

enum class NotificationType {
    IDLE,
    MESSAGE,
    MESSAGE_SENT,
    MESSAGE_RECEIVED,
    VOTE,
    VOTE_RESULT,
    VOTE_RECEIVED,
    ;

    fun toDescription(): String = when (this) {
        MESSAGE, MESSAGE_SENT, MESSAGE_RECEIVED -> "쪽지 알림"
        VOTE, VOTE_RESULT, VOTE_RECEIVED -> "투표 알림"
        IDLE -> ""
    }

    companion object {
        fun convertNotificationType(type: String): NotificationType =
            when (type) {
                MESSAGE.name -> MESSAGE
                MESSAGE_SENT.name -> MESSAGE_SENT
                MESSAGE_RECEIVED.name -> MESSAGE_RECEIVED
                VOTE.name -> VOTE
                VOTE_RESULT.name -> VOTE_RESULT
                VOTE_RECEIVED.name -> VOTE_RECEIVED
                else -> IDLE
            }
    }
}
