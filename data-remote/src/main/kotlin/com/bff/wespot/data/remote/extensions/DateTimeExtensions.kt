package com.bff.wespot.data.remote.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toISOLocalDateTime(): LocalDateTime? =
    runCatching {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    }.recoverCatching {
        val correctedString = this.replace(" ", "T")
        LocalDateTime.parse(correctedString, DateTimeFormatter.ISO_DATE_TIME)
    }.getOrNull()
