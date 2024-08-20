package com.bff.wespot.notification.common

import java.time.LocalTime

internal fun checkMessageSentTime(): Boolean {
    val currentTime = LocalTime.now()
    val eveningStartTime = LocalTime.of(17, 0)
    val nightStartTime = LocalTime.of(22, 0)

    return currentTime >= eveningStartTime && currentTime < nightStartTime
}
