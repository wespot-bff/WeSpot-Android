package com.bff.wespot.server.driven.overview

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.serverDriven.UpdateOverviewRepository
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.serverDriven.ClickAction
import com.bff.wespot.model.serverDriven.DeepLinkNavigation
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
class UpdateOverviewViewModel @Inject constructor(
    private val dynamicUiRepository: UpdateOverviewRepository,
) : BaseViewModel(), ContainerHost<UpdateOverviewUiState, UpdateOverviewSideEffect> {
    override val container = container<UpdateOverviewUiState, UpdateOverviewSideEffect>(
        UpdateOverviewUiState(),
    )

    fun onAction(action: UpdateOverviewAction) {
        when (action) {
            is UpdateOverviewAction.OnScreenEntered -> {
                handleDialogShown(type = action.notificationType)
            }
            is UpdateOverviewAction.OnButtonClicked -> handleButtonClicked(action.clickAction)
            UpdateOverviewAction.OnNavigateUpButtonClicked -> handleNavigateUpButtonClicked()
        }
    }

    private fun handleDialogShown(type: NotificationType) = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            dynamicUiRepository.getUpdateOverview(type)
                .onSuccess {
                    reduce {
                        state.copy(contents = it.data)
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

    private fun handleButtonClicked(action: ClickAction) = intent {
        when (action) {
            is DeepLinkNavigation -> {
                postSideEffect(UpdateOverviewSideEffect.NavigateDeepLink(action.deepLink))
            }
            else -> {
                postSideEffect(UpdateOverviewSideEffect.DismissDialog)
            }
        }
    }

    private fun handleNavigateUpButtonClicked() = intent {
        postSideEffect(UpdateOverviewSideEffect.DismissDialog)
    }
}
