package com.bff.wespot.message.common

import java.util.Locale

internal fun Long.convertMillisToTime(): String {
    val hours = this / 3_600_000
    val minutes = (this % 3_600_000) / 60_000
    val seconds = (this % 60_000) / 1_000

    return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
}
