package com.bff.wespot.data.remote.model.common

import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.model.common.Restriction
import com.bff.wespot.model.common.RestrictionType
import kotlinx.serialization.Serializable

@Serializable
data class RestrictionDto(
    val restrictionType: String,
    val endDate: String,
) {
    fun toRestriction(): Restriction = Restriction(
        restrictionType = RestrictionType.valueOf(restrictionType),
        endDate = endDate.toLocalDateFromDashPattern(),
    )
}