package com.bff.wespot.model.common

import java.time.LocalDate

data class Restriction(
    val restrictionType: RestrictionType,
    val releaseDate: LocalDate,
) {
    fun toKoreanDate(): String {
        return "${releaseDate.year}년 ${releaseDate.monthValue}월 ${releaseDate.dayOfMonth}일"
    }

    companion object {
        val Empty = Restriction(
            restrictionType = RestrictionType.NONE,
            releaseDate = LocalDate.now(),
        )
    }
}
