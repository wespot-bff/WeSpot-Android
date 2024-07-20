package com.bff.wespot.network.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun String.toISOLocalDateTime(): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
