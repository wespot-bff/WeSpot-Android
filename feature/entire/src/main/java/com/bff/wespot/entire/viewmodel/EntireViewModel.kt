package com.bff.wespot.entire.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.entire.state.EntireAction
import com.bff.wespot.entire.state.EntireSideEffect
import com.bff.wespot.entire.state.EntireUiState
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
class EntireViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel(), ContainerHost<EntireUiState, EntireSideEffect> {
    override val container = container<EntireUiState, EntireSideEffect>(EntireUiState())

    fun onAction(action: EntireAction) {
        when (action) {
            EntireAction.OnEntireScreenEntered, EntireAction.OnRevokeScreenEntered -> getProfile()
            EntireAction.OnRevokeButtonClicked -> revokeUser()
            EntireAction.OnSignOutButtonClicked -> signOut()
            EntireAction.OnRevokeConfirmed -> handleRevokeConfirmed()
            EntireAction.OnBlockListScreenEntered -> getUnBlockedMessage()
            EntireAction.UnBlockMessage -> unblockMessage()
            is EntireAction.OnUnBlockButtonClicked -> handleUnBlockButtonClicked(action.messageId)
            is EntireAction.OnRevokeReasonSelected -> handleRevokeReasonSelected(action.reason)
        }
    }

    private fun getProfile() = intent {
        viewModelScope.launch {
            userRepository.getProfile()
                .onSuccess { profile ->
                    reduce { state.copy(profile = profile) }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun revokeUser() = intent {
        viewModelScope.launch {
            // TODO Token 삭제
            authRepository.revoke(state.revokeReasonList)
                .onSuccess {
                    postSideEffect(EntireSideEffect.NavigateToAuth)
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun signOut() = intent {
        viewModelScope.launch {
            postSideEffect(EntireSideEffect.NavigateToAuth)
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
