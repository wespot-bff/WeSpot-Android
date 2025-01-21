package com.bff.wespot.message.state.report

import com.bff.wespot.message.model.ReportReason

data class ReportUiState(
    val messageId: Int = -1,
    val reportReason: ReportReason = ReportReason(),
    val inputReportReason: String = "",
)
