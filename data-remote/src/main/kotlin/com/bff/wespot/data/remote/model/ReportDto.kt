package com.bff.wespot.data.remote.model

import com.bff.wespot.model.ReportType
import kotlinx.serialization.Serializable

@Serializable
data class ReportDto (
    val type: ReportType,
    val targetId: Int,
)