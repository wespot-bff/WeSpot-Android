package com.bff.wespot.community.report.state

sealed interface CommunityReportSideEffect {
    data object OnBackClick : CommunityReportSideEffect
    data object OnReportSuccess : CommunityReportSideEffect
}
