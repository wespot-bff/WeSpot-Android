package com.bff.wespot.message.common

import java.time.LocalDateTime
import java.util.Locale

internal fun Long.convertMillisToTime(): String {
    val hours = this / 3_600_000
    val minutes = (this % 3_600_000) / 60_000
    val seconds = (this % 60_000) / 1_000

    return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
}

/** Example: "25. 02. 11" (yy. MM. dd format) */
internal fun LocalDateTime.toStringWithDotSeparator(): String {
    val year = String.format(Locale.getDefault(), "%02d", this.year % 100)
    val month = String.format(Locale.getDefault(), "%02d", this.monthValue)
    val day = String.format(Locale.getDefault(), "%02d", this.dayOfMonth)
    return "$year. $month. $day"
}
