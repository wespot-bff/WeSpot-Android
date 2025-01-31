package com.bff.wespot.model.notification

enum class NotificationType {
    IDLE,
    MESSAGE,
    MESSAGE_SENT,
    MESSAGE_RECEIVED,
    VOTE,
    VOTE_RESULT,
    VOTE_RECEIVED,
    PROFILE_UPDATE,
    UPDATE_REQUIRED,
    ;

    fun toDescription(): String = when (this) {
        MESSAGE, MESSAGE_SENT, MESSAGE_RECEIVED -> "쪽지 알림"
        VOTE, VOTE_RESULT, VOTE_RECEIVED -> "투표 알림"
        PROFILE_UPDATE -> "프로필 알림"
        IDLE, UPDATE_REQUIRED -> ""
    }

    fun isVoteNotificationType(): Boolean = this in listOf(VOTE, VOTE_RESULT, VOTE_RECEIVED)

    companion object {
        fun convertNotificationType(type: String): NotificationType =
            when (type) {
                MESSAGE.name -> MESSAGE
                MESSAGE_SENT.name -> MESSAGE_SENT
                MESSAGE_RECEIVED.name -> MESSAGE_RECEIVED
                VOTE.name -> VOTE
                VOTE_RESULT.name -> VOTE_RESULT
                VOTE_RECEIVED.name -> VOTE_RECEIVED
                PROFILE_UPDATE.name -> PROFILE_UPDATE
                UPDATE_REQUIRED.name -> UPDATE_REQUIRED
                else -> IDLE
            }
    }
}
