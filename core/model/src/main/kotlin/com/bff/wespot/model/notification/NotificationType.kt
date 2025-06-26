package com.bff.wespot.model.notification

enum class NotificationType {
    IDLE,
    MESSAGE,
    MESSAGE_V2,
    VOTE,
    VOTE_RESULT,
    VOTE_RECEIVED,
    PROFILE_UPDATE,
    ;

    fun toDescription(): String = when (this) {
        MESSAGE, MESSAGE_V2 -> "쪽지 알림"
        VOTE, VOTE_RESULT, VOTE_RECEIVED -> "투표 알림"
        PROFILE_UPDATE -> "프로필 알림"
        IDLE -> ""
    }

    fun isVoteNotificationType(): Boolean = this in listOf(VOTE, VOTE_RESULT, VOTE_RECEIVED)

    companion object {
        fun convertNotificationType(type: String): NotificationType = runCatching {
            NotificationType.valueOf(type)
        }.getOrDefault(IDLE)
    }
}
