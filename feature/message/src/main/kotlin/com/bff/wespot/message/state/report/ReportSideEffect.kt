package com.bff.wespot.message.state.report

sealed interface ReportSideEffect {
    data object NavigateToMessage : ReportSideEffect
}
