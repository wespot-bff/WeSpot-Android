package com.bff.wespot.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.dynamicui.DynamicUiRepository
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.state.featureoverview.FeatureOverviewAction
import com.bff.wespot.state.featureoverview.FeatureOverviewSideEffect
import com.bff.wespot.state.featureoverview.FeatureOverviewUiState
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeatureOverviewViewModel @Inject constructor(
    private val dynamicUiRepository: DynamicUiRepository,
): BaseViewModel(), ContainerHost<FeatureOverviewUiState, FeatureOverviewSideEffect> {
    override val container = container<FeatureOverviewUiState, FeatureOverviewSideEffect>(
        FeatureOverviewUiState()
    )

    fun onAction(action: FeatureOverviewAction) {
        when (action) {
            is FeatureOverviewAction.OnFeatureOverViewDialogShow -> {
                handleDialogShown(type = action.notificationType)
            }
            is FeatureOverviewAction.OnNavigateButtonClicked -> handleNavigateButtonClicked(action.deepLink)
            FeatureOverviewAction.OnDismissButtonClicked -> handleDismissButtonClicked()
        }
    }

    private fun handleDialogShown(type: NotificationType) = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            dynamicUiRepository.getFeatureOverview(type)
                .onSuccess {
                    reduce {
                        state.copy(ui = it)
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onFailure {
                    Timber.e(it)
                }
                .also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun handleDismissButtonClicked() = intent {
        postSideEffect(FeatureOverviewSideEffect.DismissDialog)
    }

    private fun handleNavigateButtonClicked(deepLink: String) = intent {
        postSideEffect(FeatureOverviewSideEffect.NavigateScreen(deepLink))
    }
}
