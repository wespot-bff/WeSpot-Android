package com.bff.wespot.message.state.report

import com.bff.wespot.message.model.ReportReason

data class ReportUiState(
    val messageId: Int,
    val reportReason: ReportReason = ReportReason(),
    val inputReportReason: String = "",
)
