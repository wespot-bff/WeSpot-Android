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
}

fun convertNotificationType(type: String): NotificationType =
    when (type) {
        NotificationType.MESSAGE.name -> NotificationType.MESSAGE
        NotificationType.MESSAGE_SENT.name -> NotificationType.MESSAGE_SENT
        NotificationType.MESSAGE_RECEIVED.name -> NotificationType.MESSAGE_RECEIVED
        NotificationType.VOTE.name -> NotificationType.VOTE
        NotificationType.VOTE_RESULT.name -> NotificationType.VOTE_RESULT
        NotificationType.VOTE_RECEIVED.name -> NotificationType.VOTE_RECEIVED
        else -> NotificationType.IDLE
    }
