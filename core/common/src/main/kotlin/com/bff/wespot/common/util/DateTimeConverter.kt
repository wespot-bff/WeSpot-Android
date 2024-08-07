package com.bff.wespot.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDate.toDateTimeString(): String =
    "${this.monthValue}월 ${this.dayOfMonth}일 ${this.dayOfWeek.name.toKoreanWeekDay()}"

fun LocalDate.toDateString(): String =
    "${this.year}-${this.monthValue}-${this.dayOfMonth}"

fun String.timeDifference(): Long {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val inputDate = LocalDate.parse(this, formatter)
    val currentDate = LocalDate.now()

    val daysDifference = ChronoUnit.DAYS.between(inputDate, currentDate)
    return daysDifference
}

fun Long.toDateString(date: String): String =
    when (this) {
        0L -> {
            "오늘"
        }
        in (1..7) -> {
            "${this}일 전"
        }
        else -> {
            date.split("-").joinToString(".")
        }
    }

private fun String.toKoreanWeekDay() =
    when (this) {
        "MONDAY" -> "월요일"
        "TUESDAY" -> "화요일"
        "WEDNESDAY" -> "수요일"
        "THURSDAY" -> "목요일"
        "FRIDAY" -> "금요일"
        "SATURDAY" -> "토요일"
        "SUNDAY" -> "일요일"
        else -> ""
    }
