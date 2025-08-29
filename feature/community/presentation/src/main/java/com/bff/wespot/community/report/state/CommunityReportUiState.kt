package com.bff.wespot.community.report.state

import com.bff.wespot.model.community.ReportReason

data class CommunityReportUiState(
    val isLoading: Boolean = false,
    val reportReasons: List<ReportReason> = emptyList(),
    val selectedReasonIds: List<Int> = emptyList(),
    val customReportText: String = "",
    val isSubmitting: Boolean = false,
)
