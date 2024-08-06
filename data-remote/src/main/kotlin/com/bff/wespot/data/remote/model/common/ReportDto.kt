package com.bff.wespot.data.remote.model.common

import com.bff.wespot.model.common.ReportType
import kotlinx.serialization.Serializable

@Serializable
data class ReportDto (
    val type: ReportType,
    val targetId: Int,
)