package com.bff.wespot.message.state.report

import androidx.annotation.StringRes

sealed interface ReportSideEffect {
    data object NavigateToMessage : ReportSideEffect
    data class ShowToast(
        @StringRes val message: Int,
    ) : ReportSideEffect
}
