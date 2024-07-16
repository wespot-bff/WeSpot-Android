package com.bff.wespot.message.model

import java.time.LocalTime

enum class TimePeriod {
    DAWN_TO_EVENING,
    EVENING_TO_NIGHT,
    NIGHT_TO_DAWN,
}

internal fun getCurrentTimePeriod(): TimePeriod {
    val currentTime = LocalTime.now()
    val dawnStartTime = LocalTime.of(0, 0)
    val eveningStartTime = LocalTime.of(17, 0)
    val nightStartTime = LocalTime.of(22, 0)

    return when {
        currentTime.isBetween(dawnStartTime, eveningStartTime) -> TimePeriod.DAWN_TO_EVENING
        currentTime.isBetween(eveningStartTime, nightStartTime) -> TimePeriod.EVENING_TO_NIGHT
        else -> TimePeriod.NIGHT_TO_DAWN
    }
}

private fun LocalTime.isBetween(startTime: LocalTime, endTime: LocalTime): Boolean {
    return this >= startTime && this < endTime
}
