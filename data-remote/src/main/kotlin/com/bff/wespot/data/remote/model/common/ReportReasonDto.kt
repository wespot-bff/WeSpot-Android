package com.bff.wespot.data.remote.model.common

import com.bff.wespot.model.community.ReportReason
import kotlinx.serialization.Serializable

@Serializable
data class ReportReasonDto(
    val id: Int,
    val reason: String,
    val isReasonEditable: Boolean = false
) {
    fun toDomain(): ReportReason = ReportReason(
        id = id,
        reason = reason,
        isReasonEditable = isReasonEditable
    )
}