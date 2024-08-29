package com.bff.wespot.entire.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.RemoteConfigRepository
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.entire.state.EntireAction
import com.bff.wespot.entire.state.EntireSideEffect
import com.bff.wespot.entire.state.EntireUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EntireViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val messageRepository: MessageRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val messageStorageRepository: MessageStorageRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel(), ContainerHost<EntireUiState, EntireSideEffect> {
    override val container = container<EntireUiState, EntireSideEffect>(EntireUiState())

    fun onAction(action: EntireAction) {
        when (action) {
            EntireAction.OnEntireScreenEntered -> {
                observeProfileDataFlow()
                fetchWebLinkFromRemoteConfig()
            }
            EntireAction.OnSettingScreenEntered -> fetchWebLinkFromRemoteConfig()
            EntireAction.OnRevokeScreenEntered -> observeProfileDataFlow()
            EntireAction.OnBlockListScreenEntered -> getUnBlockedMessage()
            EntireAction.OnRevokeConfirmed -> handleRevokeConfirmed()
            EntireAction.OnRevokeButtonClicked -> revokeUser()
            EntireAction.OnSignOutButtonClicked -> signOut()
            EntireAction.UnBlockMessage -> unblockMessage()
            is EntireAction.OnUnBlockButtonClicked -> handleUnBlockButtonClicked(action.messageId)
            is EntireAction.OnRevokeReasonSelected -> handleRevokeReasonSelected(action.reason)
        }
    }

    private fun observeProfileDataFlow() = intent {
        viewModelScope.launch {
            profileRepository.profileDataFlow
                .distinctUntilChanged()
                .catch { exception ->
                    Timber.e(exception)
                }
                .collect {
                    reduce { state.copy(profile = it) }
                }
        }
    }

    private fun fetchWebLinkFromRemoteConfig() = intent {
        val urlList = listOf(
            RemoteConfigKey.VOTE_QUESTION_GOOGLE_FORM_URL,
            RemoteConfigKey.WESPOT_KAKAO_CHANNEL_URL,
            RemoteConfigKey.WESPOT_INSTAGRAM_URL,
            RemoteConfigKey.USER_OPINION_GOOGLE_FORM_URL,
            RemoteConfigKey.RESEARCH_PARTICIPATION_GOOGLE_FORM_URL,
            RemoteConfigKey.WESPOT_MAKERS_URL,
            RemoteConfigKey.PROFILE_CHANGE_GOOGLE_FORM_URL,
            RemoteConfigKey.PRIVACY_POLICY_URL,
            RemoteConfigKey.PLAY_STORE_URL,
            RemoteConfigKey.TERMS_OF_SERVICE_URL,
        )
        val webLinkMap = urlList.associateWith { webLink ->
            remoteConfigRepository.fetchFromRemoteConfig(webLink)
        }
        reduce { state.copy(webLinkMap = webLinkMap) }
    }

    private fun revokeUser() = intent {
        viewModelScope.launch {
            launch {
                authRepository.revoke(state.revokeReasonList)
                    .onSuccess {
                        clearCachedData()
                        postSideEffect(EntireSideEffect.NavigateToAuth)
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            }
        }
    }

    private fun signOut() = intent {
        clearCachedData()
        postSideEffect(EntireSideEffect.NavigateToAuth)
    }

    private fun clearCachedData() {
        viewModelScope.launch {
            launch { dataStoreRepository.clear(DataStoreKey.PUSH_TOKEN) }
            launch { profileRepository.clearProfile() }
        }
    }

    private fun getUnBlockedMessage() = intent {
        viewModelScope.launch {
            messageRepository.getBlockedMessage(cursorId = 0) // TODO Cursor Paging
                .onSuccess { blockedMessageList ->
                    reduce { state.copy(blockedMessageList = blockedMessageList) }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleUnBlockButtonClicked(messageId: Int) = intent {
        reduce { state.copy(unBlockMessageId = messageId) }
    }

    private fun unblockMessage() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageStorageRepository.blockMessage(state.unBlockMessageId)
                .onSuccess {
                    if (state.unBlockList.contains(state.unBlockMessageId).not()) {
                        val updatedList = state.unBlockList.toMutableList().apply {
                            add(state.unBlockMessageId)
                        }
                        reduce { state.copy(unBlockList = updatedList, isLoading = false) }
                    }
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                    Timber.e(it)
                }
        }
    }

    private fun handleRevokeReasonSelected(reason: String) = intent {
        reduce {
            val updatedList = state.revokeReasonList.toMutableList().apply {
                if (contains(reason)) {
                    remove(reason)
                } else {
                    add(reason)
                }
            }
            state.copy(revokeReasonList = updatedList)
        }
    }

    private fun handleRevokeConfirmed() = intent {
        reduce {
            state.copy(revokeConfirmed = state.revokeConfirmed.not())
        }
    }
}
