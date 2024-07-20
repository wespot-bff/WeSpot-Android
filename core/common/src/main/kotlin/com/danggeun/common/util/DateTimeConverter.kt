package com.danggeun.common.util

import java.time.LocalDate

fun LocalDate.toDateTimeString(): String =
    "${this.monthValue}월 ${this.dayOfMonth}일 ${this.dayOfWeek.name.toKoreanWeekDay()}"

private fun String.toKoreanWeekDay() =
    when(this) {
        "MONDAY" -> "월요일"
        "TUESDAY" -> "화요일"
        "WEDNESDAY" -> "수요일"
        "THURSDAY" -> "목요일"
        "FRIDAY" -> "금요일"
        "SATURDAY" -> "토요일"
        "SUNDAY" -> "일요일"
        else -> ""
    }