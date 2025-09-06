package com.bff.wespot.community.report.state

sealed interface CommunityReportAction {
    data class OnReasonSelected(val reasonId: Int) : CommunityReportAction
    data class OnCustomTextChanged(val reasonId: Int, val text: String) : CommunityReportAction
    data object OnSubmitReport : CommunityReportAction
    data object OnBackClick : CommunityReportAction
    data object LoadReportReasons : CommunityReportAction
}
