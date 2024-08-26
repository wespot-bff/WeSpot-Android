package com.bff.wespot.model.common

import java.time.LocalDate

data class Restriction(
    val restrictionType: RestrictionType,
    val endDate: LocalDate,
) {
    companion object {
        val Empty = Restriction(
            restrictionType = RestrictionType.NONE,
            endDate = LocalDate.now(),
        )
    }
}