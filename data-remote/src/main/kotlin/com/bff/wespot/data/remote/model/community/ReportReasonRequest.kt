package com.bff.wespot.data.remote.model.community

import kotlinx.serialization.Serializable

@Serializable
data class ReportReasonRequest(
    val reportReasonRequests: List<ReportReasonItem>
)

@Serializable
data class ReportReasonItem(
    val reportReasonId: Long,
    val customReason: String? = null
)