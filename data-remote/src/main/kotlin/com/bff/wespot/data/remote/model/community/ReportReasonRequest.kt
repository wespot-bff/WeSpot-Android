package com.bff.wespot.data.remote.model.community

import kotlinx.serialization.Serializable

@Serializable
data class ReportReasonRequest(
    val reportReasonIds: List<Long>
)