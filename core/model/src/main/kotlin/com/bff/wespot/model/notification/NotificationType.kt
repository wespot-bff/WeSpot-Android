package com.bff.wespot.model.notification

enum class NotificationType {
    MESSAGE,
    VOTE,
    ;

    fun toDescription(): String = when (this) {
        MESSAGE -> "쪽지 알림"
        VOTE -> "투표 알림"
    }
}
