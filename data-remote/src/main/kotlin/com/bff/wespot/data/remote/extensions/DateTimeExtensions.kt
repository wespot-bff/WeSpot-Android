package com.bff.wespot.data.remote.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toISOLocalDateTime(): LocalDateTime? =
    runCatching {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    }.recoverCatching {
        val correctedString = this.replace(" ", "T")
        LocalDateTime.parse(correctedString, DateTimeFormatter.ISO_DATE_TIME)
    }.getOrNull()

fun String.toLocalDateFromDashPattern(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(this, formatter)
}
