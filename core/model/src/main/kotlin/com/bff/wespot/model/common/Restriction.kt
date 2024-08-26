package com.bff.wespot.model.common

import java.time.LocalDate

data class Restriction(
    val restrictionType: RestrictionType,
    val endDate: LocalDate,
) {
    fun toKoreanDate(): String {
        return "${endDate.year}년 ${endDate.monthValue}월 ${endDate.dayOfMonth}일"
    }

    companion object {
        val Empty = Restriction(
            restrictionType = RestrictionType.NONE,
            endDate = LocalDate.now(),
        )
    }
}
