package com.bff.wespot.community.report

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.report.state.CommunityReportAction
import com.bff.wespot.community.report.state.CommunityReportParams
import com.bff.wespot.community.report.state.CommunityReportSideEffect
import com.bff.wespot.community.report.state.CommunityReportUiState
import com.bff.wespot.community.report.state.ReportType
import com.bff.wespot.domain.repository.community.CommunityReportRepository
import com.bff.wespot.model.community.ReportReasonItem
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CommunityReportViewModel @Inject constructor(
    param: CommunityReportParams,
    private val communityReportRepository: CommunityReportRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ContainerHost<CommunityReportUiState, CommunityReportSideEffect> {
    override val container = container<CommunityReportUiState, CommunityReportSideEffect>(
        CommunityReportUiState(),
    )

    private val targetId = param.targetId
    private val reportType = param.reportType

    fun onAction(action: CommunityReportAction) {
        intent {
            when (action) {
                is CommunityReportAction.OnReasonSelected -> {
                    val currentIds = state.selectedReasonIds.toMutableList()
                    if (currentIds.contains(action.reasonId)) {
                        currentIds.remove(action.reasonId)
                    } else {
                        currentIds.add(action.reasonId)
                    }
                    reduce { state.copy(selectedReasonIds = currentIds) }
                }

                is CommunityReportAction.OnCustomTextChanged -> {
                    reduce {
                        state.copy(
                            customReportTexts = if (action.text.isEmpty()) {
                                state.customReportTexts - action.reasonId
                            } else {
                                state.customReportTexts + (action.reasonId to action.text)
                            },
                        )
                    }
                }

                is CommunityReportAction.OnSubmitReport -> {
                    submitReport()
                }

                is CommunityReportAction.OnBackClick -> {
                    postSideEffect(CommunityReportSideEffect.OnBackClick)
                }

                is CommunityReportAction.LoadReportReasons -> {
                    loadReportReasons()
                }
            }
        }
    }

    private fun loadReportReasons() {
        intent {
            reduce { state.copy(isLoading = true) }

            viewModelScope.launch(ioDispatcher) {
                communityReportRepository.getReportReasons()
                    .onNetworkFailure {
                        postSideEffect(it.toSideEffect())
                    }
                    .onSuccess { reasons ->
                        intent {
                            reduce {
                                state.copy(
                                    isLoading = false,
                                    reportReasons = reasons,
                                )
                            }
                        }
                    }
                    .onFailure {
                        intent {
                            reduce { state.copy(isLoading = false) }
                        }
                    }
            }
        }
    }

    private fun submitReport() {
        intent {
            val reasonIds = state.selectedReasonIds
            val customReportTexts = state.customReportTexts

            if (reasonIds.isEmpty() && customReportTexts.isEmpty()) return@intent

            reduce { state.copy(isSubmitting = true) }

            viewModelScope.launch(ioDispatcher) {
                val editableReasonIds = state.reportReasons
                    .filter { it.isReasonEditable }
                    .map { it.id }
                    .toSet()

                val reportItems = buildList {
                    reasonIds.forEach { reasonId ->
                        if (reasonId in editableReasonIds) {
                            val customText = customReportTexts[reasonId]
                            if (!customText.isNullOrEmpty()) {
                                add(
                                    ReportReasonItem(
                                        reportReasonId = reasonId.toLong(),
                                        customReason = customText,
                                    ),
                                )
                            }
                        } else {
                            add(
                                ReportReasonItem(
                                    reportReasonId = reasonId.toLong(),
                                    customReason = null,
                                ),
                            )
                        }
                    }
                }

                val result = when (reportType) {
                    ReportType.POST -> communityReportRepository.reportPost(
                        targetId,
                        reportItems,
                    )

                    ReportType.COMMENT -> communityReportRepository.reportComment(
                        targetId,
                        reportItems,
                    )
                }

                result
                    .onNetworkFailure {
                        postSideEffect(it.toSideEffect())
                    }
                    .onSuccess {
                        intent {
                            reduce { state.copy(isSubmitting = false) }
                            postSideEffect(CommunityReportSideEffect.OnReportSuccess)
                        }
                    }
                    .onFailure {
                        intent {
                            reduce { state.copy(isSubmitting = false) }
                        }
                    }
            }
        }
    }
}
