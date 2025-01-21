package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.message.R
import com.bff.wespot.message.model.ReportReason
import com.bff.wespot.message.state.report.ReportAction
import com.bff.wespot.message.state.report.ReportSideEffect
import com.bff.wespot.message.state.report.ReportUiState
import com.bff.wespot.model.common.ReportType
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
) : BaseViewModel(), ContainerHost<ReportUiState, ReportSideEffect> {
    override val container = container<ReportUiState, ReportSideEffect>(ReportUiState())

    fun onAction(action: ReportAction) {
        when (action) {
            is ReportAction.OnMessageReportScreenEntered -> {
                handleMessageReportScreenEntered(action.messageId)
            }
            is ReportAction.OnReportReasonSelected -> handleReportReasonSelected(action.reportReason)
            is ReportAction.OnReportReasonChanged -> handleReportReasonChanged(action.reason)
            ReportAction.OnMessageReportButtonClicked -> reportMessage()
        }
    }

    private fun handleMessageReportScreenEntered(messageId: Int) = intent {
        /** MessageReportScreen이 Dialog로, 보이지 않아도 상태가 유지되므로 진입시에 상태를 초기화 한다.*/
        reduce {
            ReportUiState(messageId = messageId)
        }
    }

    private fun handleReportReasonSelected(reportReason: ReportReason) = intent {
        reduce {
            /** 이미 선택된 신고 사유를 클릭한 경우 선택 해제됨. */
            state.copy(
                reportReason = if (state.reportReason.index == reportReason.index) {
                    ReportReason()
                } else {
                    reportReason
                },
            )
        }
    }

    private fun handleReportReasonChanged(reason: String) = intent {
        if (reason.length <= 100) {
            reduce {
                state.copy(inputReportReason = reason)
            }
        }
    }

    private fun reportMessage() = intent {
        viewModelScope.launch {
            val reason = state.inputReportReason.ifEmpty { state.reportReason.reason }
            commonRepository.sendReport(
                report = ReportType.MESSAGE,
                targetId = state.messageId,
                content = reason,
            ).onSuccess {
                postSideEffect(ReportSideEffect.ShowToast(R.string.report_message_success))
                postSideEffect(ReportSideEffect.NavigateToMessage)
            }.onNetworkFailure {
                postSideEffect(it.toSideEffect())
            }
        }
    }
}
