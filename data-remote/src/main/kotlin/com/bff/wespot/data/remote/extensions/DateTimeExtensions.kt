package com.bff.wespot.data.remote.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toISOLocalDateTime(): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
