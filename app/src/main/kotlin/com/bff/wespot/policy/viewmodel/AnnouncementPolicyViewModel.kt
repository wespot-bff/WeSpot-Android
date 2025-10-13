package com.bff.wespot.policy.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.policy.state.AnnouncementPolicyAction
import com.bff.wespot.policy.state.AnnouncementPolicySideEffect
import com.bff.wespot.policy.state.AnnouncementPolicyUiState
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.ui.util.KakaoLoginManager
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
class AnnouncementPolicyViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val profileRepository: ProfileRepository,
) : BaseViewModel(),
    ContainerHost<AnnouncementPolicyUiState, AnnouncementPolicySideEffect> {
    override val container = container<AnnouncementPolicyUiState, AnnouncementPolicySideEffect>(
        AnnouncementPolicyUiState(),
    )

    fun onAction(action: AnnouncementPolicyAction) {
        when (action) {
            AnnouncementPolicyAction.OnScreenEntered -> fetchWebLinkFromRemoteConfig()
            AnnouncementPolicyAction.OnThreeDotClicked -> handleThreeDotClicked()
            AnnouncementPolicyAction.OnBottomSheetDismissed -> handleBottomSheetDismissed()
            AnnouncementPolicyAction.OnRevokeClicked -> handleRevokeClicked()
            AnnouncementPolicyAction.OnConfirmBottomSheetDismissed -> handleConfirmBottomSheetDismissed()
            AnnouncementPolicyAction.OnRevokeConfirmed -> handleRevokeConfirmed()
            AnnouncementPolicyAction.OnConfirmRevokeClicked -> handleConfirmRevokeClicked()
            AnnouncementPolicyAction.OnDialogDismissed -> handleDialogDismissed()
            AnnouncementPolicyAction.OnFinalRevokeClicked -> handleFinalRevoke()
        }
    }

    private fun fetchWebLinkFromRemoteConfig() = intent {
        val urlList = listOf(
            RemoteConfigKey.TERMS_OF_SERVICE_URL,
            RemoteConfigKey.PRIVACY_POLICY_URL,
            RemoteConfigKey.COMMUNITY_POLICY_URL,
        )
        val webLinkMap = urlList.associateWith { webLink ->
            remoteConfigRepository.fetchFromRemoteConfig(webLink)
        }
        reduce { state.copy(webLinkMap = webLinkMap) }
    }

    private fun handleThreeDotClicked() = intent {
        reduce { state.copy(isBottomSheetShown = true) }
    }

    private fun handleBottomSheetDismissed() = intent {
        reduce { state.copy(isBottomSheetShown = false) }
    }

    private fun handleRevokeClicked() = intent {
        reduce {
            state.copy(
                isBottomSheetShown = false,
                isConfirmBottomSheetShown = true,
            )
        }
    }

    private fun handleConfirmBottomSheetDismissed() = intent {
        reduce { state.copy(isConfirmBottomSheetShown = false) }
    }

    private fun handleRevokeConfirmed() = intent {
        reduce {
            state.copy(revokeConfirmed = !state.revokeConfirmed)
        }
    }

    private fun handleConfirmRevokeClicked() = intent {
        reduce {
            state.copy(
                isConfirmBottomSheetShown = false,
                isDialogShown = true,
            )
        }
    }

    private fun handleDialogDismissed() = intent {
        reduce { state.copy(isDialogShown = false) }
    }

    private fun handleFinalRevoke() = intent {
        reduce { state.copy(isLoading = true, isDialogShown = false) }

        viewModelScope.launch(coroutineDispatcher) {
            authRepository
                .revoke(emptyList())
                .onSuccess {
                    KakaoLoginManager.revoke()
                    clearCachedData()
                    postSideEffect(AnnouncementPolicySideEffect.NavigateToAuth)
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.onFailure {
                    reduce { state.copy(isLoading = false) }
                    Timber.e(it)
                }
        }
    }

    private fun clearCachedData() {
        viewModelScope.launch {
            launch { dataStoreRepository.clear() }
            launch { profileRepository.clearProfile() }
        }
    }
}
