package com.bff.wespot.community.report.state

import com.bff.wespot.analytics.AnalyticsEvent
import com.bff.wespot.model.community.ReportReason

data class CommunityReportUiState(
    val isLoading: Boolean = false,
    val reportReasons: List<ReportReason> = emptyList(),
    val selectedReasonIds: List<Int> = emptyList(),
    val customReportTexts: Map<Int, String> = emptyMap(),
    val isSubmitting: Boolean = false,
) {
    fun getSelectedReasonParams(): List<AnalyticsEvent.Param> {
        val reasonById = reportReasons.associateBy(ReportReason::id)

        return buildList {
            selectedReasonIds.forEachIndexed { index, id ->
                val reason = reasonById[id]
                if (reason?.isReasonEditable == true) {
                    val custom = customReportTexts[id].orEmpty()
                    if (custom.isNotBlank()) {
                        add(AnalyticsEvent.Param("reason${index + 1}_custom", custom))
                    }
                } else {
                    add(AnalyticsEvent.Param("reason${index + 1}", reason?.reason.orEmpty()))
                }
            }
        }
    }
}
