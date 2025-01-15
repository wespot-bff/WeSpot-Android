package com.bff.wespot.message.state.report

import com.bff.wespot.message.model.ReportReason

sealed class ReportAction {
    data object OnMessageReportButtonClicked : ReportAction()
    data class OnMessageReportScreenEntered(val messageId: Int) : ReportAction()
    data class OnReportReasonSelected(val reportReason: ReportReason) : ReportAction()
    data class OnReportReasonChanged(val reason: String) : ReportAction()
}
