package com.bff.wespot.community.report.state

enum class ReportType {
    POST,
    COMMENT,
}

data class CommunityReportParams(
    val targetId: String,
    val reportType: ReportType = ReportType.POST,
)
